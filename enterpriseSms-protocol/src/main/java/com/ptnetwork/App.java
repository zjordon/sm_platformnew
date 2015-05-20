package com.ptnetwork;

//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ptnetwork.enterpriseSms.protocol.ActiveTestPacket;
import com.ptnetwork.enterpriseSms.protocol.ActiveTestRespPacket;
import com.ptnetwork.enterpriseSms.protocol.DeliverPacket;
import com.ptnetwork.enterpriseSms.protocol.DeliverRespPacket;
import com.ptnetwork.enterpriseSms.protocol.LoginPacket;
import com.ptnetwork.enterpriseSms.protocol.LoginRespPacket;
import com.ptnetwork.enterpriseSms.protocol.MsgCommand;
import com.ptnetwork.enterpriseSms.protocol.PacketHead;
import com.ptnetwork.enterpriseSms.protocol.Utils;

/**
 * Hello world!
 *
 */
public class App {
	private boolean stop = false;
	private Socket msgSocket;
	private InputStream in;
	private OutputStream out;
//	private DataInputStream in;
//	private DataOutputStream out;

	public static void main(String[] args) throws Exception {
		App app = new App();
		app.initSocket();
		boolean result = app.connectISMG();
		int count = 0;
		while (!result) {
			count++;
			result = app.connectISMG();
			if (count > 2) {
				break;
			}
		}
		if (result) {
			app.startReceiveMsgThread();
			app.startActivityThread();
		} else {
			app.console("登录失败");
		}
	}

	private void startReceiveMsgThread() {
		(new Thread(new ReciveMsgRunnable())).start();
	}

	private void startActivityThread() {
		(new Thread(new ActivityRunnable())).start();
	}

	private void initSocket() {
		if (null == msgSocket || msgSocket.isClosed()
				|| !msgSocket.isConnected()) {
			try {
				msgSocket = new Socket("211.143.170.161", 8855);
				msgSocket.setKeepAlive(true);
				msgSocket.setSoTimeout(180000);
				this.in = this.msgSocket.getInputStream();
				//this.in=new DataInputStream(this.msgSocket.getInputStream());
				this.out = this.msgSocket.getOutputStream();
				//this.out=new DataOutputStream(this.msgSocket.getOutputStream());
			} catch (UnknownHostException e) {
				console("Socket链接短信网关端口号不正确：" + e.getMessage());
				// 链接短信网关
			} catch (IOException e) {
				console("Socket链接短信网关失败：" + e.getMessage());
			}
		}
	}

	private boolean connectISMG() throws IOException {
		boolean loginSuccess = false;
		console("请求连接到ISMG...");
		LoginPacket loginPacket = new LoginPacket();
		loginPacket.setClientId("413070");
		loginPacket.setVersion((byte) 0x20);
		loginPacket.setTimestamp(Utils.getTimestamp());
		loginPacket.encodeAuthClient("Lf4GFYev");
		this.out.write(loginPacket.encode());
		this.out.flush();
		int packetLength = Utils.bytes2int(in);
		if (packetLength > 0) {
			byte[] data = new byte[packetLength];
			Utils.int2bytes(packetLength, data, 0);
			in.read(data, 4, packetLength - 4);
			PacketHead packetHead = new PacketHead();
			packetHead.setPacketLength(packetLength);
			packetHead.decode(data);
			switch (packetHead.getRequestId()) {
			case MsgCommand.CMPP_CONNECT_RESP:
				LoginRespPacket loginRespPacket = new LoginRespPacket(
						packetHead);
				//loginRespPacket.decode(in);
				loginRespPacket.decode(data);
				if (loginRespPacket.getStatus() == 0) {
					loginSuccess = true;
				}
				break;
			default:
				console("返回其它类型的包，命令格式为:" + packetHead.getRequestId());
				break;
			}
		}

		return loginSuccess;
	}

	private void receiveMsg() throws Exception {
		while (!this.stop) {
			int packetLength = Utils.bytes2int(in);
			//int packetLength = this.in.readInt();
			if (packetLength > 0) {
				this.processMsg(packetLength);
			}

		}
	}

	private void processMsg(int packetLength) throws Exception {

		// int packetLength = Utils.bytes2int(in, (byte)firstByte);
		byte[] data = new byte[packetLength];
		Utils.int2bytes(packetLength, data, 0);
		this.in.read(data, 4, packetLength - 4);
		PacketHead packetHead = new PacketHead();
		packetHead.setPacketLength(packetLength);
		// packetHead.decode(in);
		packetHead.decode(data);
		switch (packetHead.getRequestId()) {
		case MsgCommand.CMPP_CONNECT_RESP:
			LoginRespPacket loginRespPacket = new LoginRespPacket(packetHead);
			// loginRespPacket.decode(in);
			loginRespPacket.decode(data);
			console("CMPP_CONNECT_RESP,status :" + loginRespPacket.getStatus()
					+ " 序列号：" + loginRespPacket.getSequenceId());
			break;
		case MsgCommand.CMPP_ACTIVE_TEST_RESP:

			console("短信网关与短信网关进行连接检查" + " 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_SUBMIT_RESP:
			console("CMPP_SUBMIT_RESP");
			break;
		case MsgCommand.CMPP_TERMINATE_RESP:
			console("拆除与ISMG的链接" + " 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_CANCEL_RESP:
			console("CMPP_CANCEL_RESP 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_CANCEL:
			console("CMPP_CANCEL 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_DELIVER:
			console("短信来了");
			DeliverPacket deliverPacket = new DeliverPacket(packetHead);
			// deliverPacket.decode(in);
			deliverPacket.decode(data);
			console("发送号码为:" + deliverPacket.getSrcTermId() + ",消息内容为:"
					+ deliverPacket.getMsgContent() + ",msgId:" + deliverPacket.getMsgId() + ",sequenceId:" + deliverPacket.getSequenceId());
			DeliverRespPacket deliverRespPacket = new DeliverRespPacket(
					deliverPacket.getSequenceId());
			deliverRespPacket.setMsgId(deliverPacket.getMsgId());
			deliverRespPacket.setStatus((byte) 0);
			System.out.println("返回消息包,msgId:" + deliverRespPacket.getMsgId() + ", sequenceId:" + deliverRespPacket.getSequenceId());
			byte[] bs = deliverRespPacket.encode();
			for (byte b : bs) {
				System.out.println(b);
			}
			this.out.write(bs);
			//this.out.flush();
			break;
		case MsgCommand.CMPP_DELIVER_RESP:
			console("CMPP_DELIVER_RESP 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_QUERY:
			console("CMPP_QUERY 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_QUERY_RESP:
			console("CMPP_QUERY_RESP 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_TERMINATE:
			console("CMPP_TERMINATE 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_CONNECT:
			console("CMPP_CONNECT 序列号：" + packetHead.getSequenceId());
			break;
		case MsgCommand.CMPP_ACTIVE_TEST:
			console("CMPP_ACTIVE_TEST 序列号：" + packetHead.getSequenceId());
			ActiveTestRespPacket activeResp = new ActiveTestRespPacket(
					packetHead.getSequenceId());
			bs = activeResp.encode();
//			for (byte b : bs) {
//				System.out.println(b);
//			}
			this.out.write(bs);
			break;
		case MsgCommand.CMPP_SUBMIT:
			console("CMPP_SUBMIT 序列号：" + packetHead.getSequenceId());
			break;
		default:
			console("无法解析IMSP返回的包结构：包长度为" + packetHead.getPacketLength());
			break;
		}

	}

	private void console(String msg) {
		System.out.println(msg);
	}

	/**
	 * 链路检查
	 * 
	 * @return
	 */
	private void activityTestISMG() {
		try {
			ActiveTestPacket activeTestPacket = new ActiveTestPacket();
			System.out.println("序列号:" + activeTestPacket.getSequenceId());
			this.out.write(activeTestPacket.encode());
			//this.out.flush();
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				out = null;
			}
			console("链路检查出错:" + e.getMessage());
		}
	}

	/**
	 * 短信接口长链接，定时进行链路检查
	 */
	private void executeInternal() {
		console("×××××××××××××开始链路检查××××××××××××××");
		// int count=0;
		// boolean result=this.activityTestISMG();
		// while(!result){
		// count++;
		// result=this.activityTestISMG();
		// if(count>=(MsgConfig.getConnectCount()-1)){//如果再次链路检查次数超过两次则终止连接
		// break;
		// }
		// }
		this.activityTestISMG();
		console("×××××××××××××链路检查结束××××××××××××××");
	}

	private class ReciveMsgRunnable implements Runnable {

		private ReciveMsgRunnable() {
		}

		@Override
		public void run() {
			try {
				App.this.receiveMsg();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private class ActivityRunnable implements Runnable {

		@Override
		public void run() {
			while (!App.this.stop) {
				App.this.executeInternal();
				try {
					Thread.sleep(180000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
