/**
 * 
 */
package com.ptnetwork;

//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ptnetwork.enterpriseSms.protocol.AbstractPacket;
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
 * 异步处理测试
 * @author jasonzhang
 *
 */
public class AppAsync {

	private boolean stop = false;
	private Socket msgSocket;
	private InputStream in;
	private OutputStream out;
//	private DataInputStream in;
//	private DataOutputStream out;
	
	private ConcurrentLinkedQueue<byte[]> dataQueue = new ConcurrentLinkedQueue<byte[]>();
	private ConcurrentLinkedQueue<AbstractPacket> sendDataQueue = new ConcurrentLinkedQueue<AbstractPacket>();
	
	//链路最后活跃时间，用于链路测试
	private long latestBusyTime = 0;
	//最后一次发出链路测试的时间
	private long latestTestActivityTime = 0;
	//链路测试的次数
	private int testAcitvityNum = 0;

	public static void main(String[] args) throws Exception {
		AppAsync app = new AppAsync();
		app.login();
	}
	
	private void login() throws IOException {
		this.initSocket();
		boolean result = this.connectISMG();
		int count = 0;
		while (!result) {
			count++;
			result = this.connectISMG();
			if (count > 2) {
				break;
			}
		}
		if (result) {
			this.stop = false;
			this.startReceiveMsgThread();
			this.startSendDataThread();
			this.startProcessDataListThread();
		} else {
			this.console("登录失败");
		}
	}

	private void startReceiveMsgThread() {
		(new Thread(new ReciveMsgTask())).start();
	}

	private void startSendDataThread() {
		(new Thread(new SendDataTask())).start();
	}
	
	private void startProcessDataListThread() {
		(new Thread(new ProcessDataListTask())).start();
	}

	private void initSocket() {
		if (null == msgSocket || msgSocket.isClosed()
				|| !msgSocket.isConnected()) {
			try {
				msgSocket = new Socket("211.143.170.161", 8855);
				msgSocket.setKeepAlive(true);
				msgSocket.setSoTimeout(180000);
				this.in = this.msgSocket.getInputStream();
//				this.in=new DataInputStream(this.msgSocket.getInputStream());
				this.out = this.msgSocket.getOutputStream();
//				this.out=new DataOutputStream(this.msgSocket.getOutputStream());
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
			//更新最后活跃时间
			this.latestBusyTime = System.currentTimeMillis();
		}

		return loginSuccess;
	}

	private void receiveMsg() throws Exception {
		while (!this.stop) {
			int packetLength = Utils.bytes2int(in);
			//int packetLength = this.in.readInt();
			if (packetLength > 0) {
				//this.processMsg(packetLength);
				byte[] data = new byte[packetLength];
				Utils.int2bytes(packetLength, data, 0);
				this.in.read(data, 4, packetLength - 4);
				this.dataQueue.add(data);
				//更新最后活跃时间
				this.latestBusyTime = System.currentTimeMillis();
			}

		}
	}
	/**
	 * 负责发送数据包以及链接测试
	 * @throws Exception
	 */
	private void sendMsg() throws Exception {
		while (!stop) {
			int sendNum = this.processSendDataList();
			if (sendNum > 0) {
				console("send data num is " + sendNum);
			} else {
				long currentTime = System.currentTimeMillis();
				//如果上一次链路测试发出后60秒内未收到链路测试的回应包
				if (this.testAcitvityNum > 1 && currentTime - this.latestTestActivityTime > 60) {
					//如果链路测试次数超过2次则直接重新连接网关
					if (this.testAcitvityNum > 2) {
						//重新连网关
						this.reLogin();
					} else {
						//再次发送链路测试包
						ActiveTestPacket activeTestPacket = new ActiveTestPacket();
						this.sendDataQueue.add(activeTestPacket);
					}
				} else if (currentTime - this.latestTestActivityTime > 180000 && this.latestBusyTime <= this.latestTestActivityTime) {
					//如果离上一次链路测试时间有3分钟并且链路的最后活跃时间小于最后的链路测试时间则发送一次链路测试
					console("the last busy time is too long send activite test");
					ActiveTestPacket activeTestPacket = new ActiveTestPacket();
					this.sendDataQueue.add(activeTestPacket);
				} else {
					console("no data to send sleep 10 secs");
					Thread.sleep(10000);
				}
			}
		}
	}
	
	
	private void reLogin() throws IOException {
		this.stop = true;
		if (this.msgSocket != null && !this.msgSocket.isClosed()) {
			this.msgSocket.close();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.login();
		
	}
	
	private int processSendDataList() throws Exception {
		ArrayList<AbstractPacket> dataList = new ArrayList<AbstractPacket>(100);
		for (int i=0; i<100; i++) {
			AbstractPacket data = this.sendDataQueue.poll();
			if (data != null) {
				dataList.add(data);
			} else {
				break;
			}
		}
		if (!dataList.isEmpty()) {
			for (AbstractPacket data : dataList) {
				this.out.write(data.encode());
				if (data.getRequestId() == MsgCommand.CMPP_ACTIVE_TEST) {
					//如果是链路测试，则更新链路测试的时间和次数
					this.latestTestActivityTime = System.currentTimeMillis();
					this.testAcitvityNum++;
					console("send activity test, serial is " + data.getSequenceId());
				}
			}
			//更新最后活跃时间
			this.latestBusyTime = System.currentTimeMillis();
		} else {
			console("send dataList is empty");
		}
		return dataList.size();
	}
	
	private void processDataList()throws Exception {
		while (!this.stop) {
			ArrayList<byte[]> dataList = new ArrayList<byte[]>(100);
			for (int i=0; i<100; i++) {
				byte[] data = this.dataQueue.poll();
				if (data != null) {
					dataList.add(data);
				} else {
					break;
				}
			}
			if (!dataList.isEmpty()) {
				for (byte[] data : dataList) {
					this.processMsg(data);
				}
			} else {
				console("dataList is empty sleep 10 secs");
				Thread.sleep(10000);
			}
		}
	}

	private void processMsg(byte[] data) throws Exception {

		// int packetLength = Utils.bytes2int(in, (byte)firstByte);
		PacketHead packetHead = new PacketHead();
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
			//收到链路测试回应包，把测试数目改为0
			this.testAcitvityNum = 0;
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
//			System.out.println("返回消息包,msgId:" + deliverRespPacket.getMsgId() + ", sequenceId:" + deliverRespPacket.getSequenceId());
//			byte[] bs = deliverRespPacket.encode();
//			for (byte b : bs) {
//				System.out.println(b);
//			}
//			this.out.write(bs);
//			this.out.flush();
			this.sendDataQueue.add(deliverRespPacket);
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
			//bs = activeResp.encode();
//			for (byte b : bs) {
//				System.out.println(b);
//			}
			//this.out.write(bs);
			this.sendDataQueue.add(activeResp);
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

	private class ReciveMsgTask implements Runnable {

		private ReciveMsgTask() {
		}

		@Override
		public void run() {
			try {
				AppAsync.this.receiveMsg();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private class SendDataTask implements Runnable {

		@Override
		public void run() {
			
				try {
					AppAsync.this.sendMsg();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

		}

	}
	
	private class ProcessDataListTask implements Runnable {

		@Override
		public void run() {
			try {
				AppAsync.this.processDataList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
