/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.protocol.AbstractPacket;
import com.ptnetwork.enterpriseSms.protocol.ActiveTestPacket;
import com.ptnetwork.enterpriseSms.protocol.ActiveTestRespPacket;
import com.ptnetwork.enterpriseSms.protocol.DeliverPacket;
import com.ptnetwork.enterpriseSms.protocol.DeliverRespPacket;
import com.ptnetwork.enterpriseSms.protocol.ExitPacket;
import com.ptnetwork.enterpriseSms.protocol.LoginPacket;
import com.ptnetwork.enterpriseSms.protocol.LoginRespPacket;
import com.ptnetwork.enterpriseSms.protocol.MsgCommand;
import com.ptnetwork.enterpriseSms.protocol.PacketHead;
import com.ptnetwork.enterpriseSms.protocol.Utils;

/**
 * @author jasonzhang
 *
 */
public class Kernel {
	private static final Logger logger = Logger.getLogger(Kernel.class);
	
	private final static Kernel instance = new Kernel();
	
	public final static Kernel getInstance() {
		return instance;
	}
	
	private Kernel(){}
	
	private Config config;
	private DeliverListener deliverListener;

	private Socket msgSocket;
	private InputStream in;
	private OutputStream out;

	private ConcurrentLinkedQueue<byte[]> dataQueue = new ConcurrentLinkedQueue<byte[]>();
	private ConcurrentLinkedQueue<AbstractPacket> sendDataQueue = new ConcurrentLinkedQueue<AbstractPacket>();

	// 链路最后活跃时间，用于链路测试
	private long latestBusyTime = 0;
	// 最后一次发出链路测试的时间
	private long latestTestActivityTime = 0;
	// 链路测试的次数
	private int testAcitvityNum = 0;
	//是否停止接收数据包的任务
	private boolean stopReceiveData;
	//是否停止处理数据包的任务
	private boolean stopProcessData;
	//是否停止发送数据包的任务
	private boolean stopSendData;

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setDeliverListener(DeliverListener deliverListener) {
		this.deliverListener = deliverListener;
	}

	public void init() {
		if (this.config != null) {
			if (this.initSocket()) {
				boolean result = this.connectISMG();
				int count = 0;
				while (!result) {
					count++;
					result = this.connectISMG();
					if (count > this.config.getConnectCount()) {
						break;
					}
				}
				if (result) {
					this.startReceiveDataTask();
					this.startProcessDataTask();
					this.startSendDataTask();
				} else {
					logger.warn("connect to ismg fail");
				}
			}
		}
	}

	public void destory() {

	}

	public void stop() {
		//发送退出包
		ExitPacket exitPacket = new ExitPacket();
		this.sendDataQueue.add(exitPacket);
	}

	private boolean initSocket() {
		boolean success = true;
		if (null == msgSocket || msgSocket.isClosed()
				|| !msgSocket.isConnected()) {
			try {
				msgSocket = new Socket(this.config.getIsmgIp(), this.config.getIsmgPort());
				msgSocket.setKeepAlive(true);
				msgSocket.setSoTimeout(180000);
				this.in = this.msgSocket.getInputStream();
				this.out = this.msgSocket.getOutputStream();
			} catch (UnknownHostException e) {
				logger.error("initSocket error", e);
				success = false;
			} catch (IOException e) {
				logger.error("initSocket error", e);
				success = false;
			}
		}
		return success;
	}

	private boolean connectISMG() {
		boolean loginSuccess = false;
		logger.info("connect to ISMG...");
		LoginPacket loginPacket = new LoginPacket();
		loginPacket.setClientId(this.config.getSpId());
		loginPacket.setVersion((byte) 0x20);
		loginPacket.setTimestamp(Utils.getTimestamp());
		loginPacket.encodeAuthClient(this.config.getSharedSecret());
		try {
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
					loginRespPacket.decode(data);
					if (loginRespPacket.getStatus() == 0) {
						loginSuccess = true;
					}
					break;
				default:
					logger.info("connect return data is no correct with :"
							+ packetHead.getRequestId());
					break;
				}
				// 更新最后活跃时间
				this.latestBusyTime = System.currentTimeMillis();
			}
		} catch (IOException e) {
			logger.error("connectISMG error", e);
			loginSuccess = false;
		}
		

		return loginSuccess;
	}
	
	private void startReceiveDataTask() {
		this.stopReceiveData = false;
		(new Thread(new ReciveDataTask())).start();
	}
	
	private void startProcessDataTask() {
		this.stopProcessData = false;
		(new Thread(new ProcessDataTask())).start();
	}
	
	private void startSendDataTask() {
		this.stopSendData = false;
		(new Thread(new SendDataTask())).start();
	}
	
	private void receiveData() throws IOException {
		while (!this.stopReceiveData) {
			int packetLength = Utils.bytes2int(in);
			if (packetLength > 0) {
				byte[] data = new byte[packetLength];
				Utils.int2bytes(packetLength, data, 0);
				this.in.read(data, 4, packetLength - 4);
				this.dataQueue.add(data);
				//更新最后活跃时间
				this.latestBusyTime = System.currentTimeMillis();
			}
		}
	}
	
	private void processData() throws IOException {
		while (!this.stopProcessData) {
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
				logger.info("dataList is empty sleep 10 secs");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					logger.error("processData error", e);
				}
			}
		}
	}
	
	private void processMsg(byte[] data) throws IOException {
				PacketHead packetHead = new PacketHead();
				packetHead.decode(data);
				switch (packetHead.getRequestId()) {
				case MsgCommand.CMPP_DELIVER:
					DeliverPacket deliverPacket = new DeliverPacket(packetHead);
					deliverPacket.decode(data);
					logger.info("msisdn is :" + deliverPacket.getSrcTermId() + ",msgcontent:"
							+ deliverPacket.getMsgContent() + ",msgId:" + deliverPacket.getMsgId() + ",sequenceId:" + deliverPacket.getSequenceId());
					DeliverRespPacket deliverRespPacket = new DeliverRespPacket(
							deliverPacket.getSequenceId());
					deliverRespPacket.setMsgId(deliverPacket.getMsgId());
					deliverRespPacket.setStatus((byte) 0);
					this.sendDataQueue.add(deliverRespPacket);
					if (this.deliverListener != null) {
						this.deliverListener.addDeliver(deliverPacket);
					}
					break;
				case MsgCommand.CMPP_ACTIVE_TEST:
					logger.info("CMPP_ACTIVE_TEST 序列号：" + packetHead.getSequenceId());
					ActiveTestRespPacket activeResp = new ActiveTestRespPacket(
							packetHead.getSequenceId());
					this.sendDataQueue.add(activeResp);
					break;
					case MsgCommand.CMPP_TERMINATE_RESP:
					logger.info("CMPP_TERMINATE_RESP 序列号：" + packetHead.getSequenceId());
					//接收到退出响应包,停止接收信息和处理信息任务，并断开socket
					this.stopReceiveData = true;
					this.stopProcessData = true;
					this.in.close();
					this.out.close();
					this.msgSocket.close();
					break;
//				case MsgCommand.CMPP_CONNECT_RESP:
//					LoginRespPacket loginRespPacket = new LoginRespPacket(packetHead);
//					loginRespPacket.decode(data);
//					logger.info("CMPP_CONNECT_RESP,status :" + loginRespPacket.getStatus()
//							+ " 序列号：" + loginRespPacket.getSequenceId());
//					break;
//				case MsgCommand.CMPP_ACTIVE_TEST_RESP:
//					//收到链路测试回应包，把测试数目改为0
//					this.testAcitvityNum = 0;
//					logger.info("短信网关与短信网关进行连接检查" + " 序列号：" + packetHead.getSequenceId());
//					break;
//				case MsgCommand.CMPP_SUBMIT_RESP:
//					logger.info("CMPP_SUBMIT_RESP");
//					break;
//				case MsgCommand.CMPP_TERMINATE_RESP:
//					logger.info("拆除与ISMG的链接" + " 序列号：" + packetHead.getSequenceId());
//					break;
//				case MsgCommand.CMPP_CANCEL:
//					logger.info("CMPP_CANCEL 序列号：" + packetHead.getSequenceId());
//					break;
//				
//				case MsgCommand.CMPP_DELIVER_RESP:
//					logger.info("CMPP_DELIVER_RESP 序列号：" + packetHead.getSequenceId());
//					break;
//				case MsgCommand.CMPP_QUERY:
//					logger.info("CMPP_QUERY 序列号：" + packetHead.getSequenceId());
//					break;
//				case MsgCommand.CMPP_QUERY_RESP:
//					logger.info("CMPP_QUERY_RESP 序列号：" + packetHead.getSequenceId());
//					break;
//				case MsgCommand.CMPP_TERMINATE:
//					logger.info("CMPP_TERMINATE 序列号：" + packetHead.getSequenceId());
//					break;
//				case MsgCommand.CMPP_CONNECT:
//					logger.info("CMPP_CONNECT 序列号：" + packetHead.getSequenceId());
//					break;
//				
//				case MsgCommand.CMPP_SUBMIT:
//					logger.info("CMPP_SUBMIT 序列号：" + packetHead.getSequenceId());
//					break;
				default:
					logger.info("other packet：packet length is " + packetHead.getPacketLength() + ",packet command is " + packetHead.getRequestId());
					break;
				}
	}
	
	private void sendData() throws IOException {
		while (!this.stopSendData) {
			int sendNum = this.processSendDataList();
			if (sendNum > 0) {
				logger.info("send data num is " + sendNum);
			} else {
				long currentTime = System.currentTimeMillis();
				//如果上一次链路测试发出后60秒内未收到链路测试的回应包
				if (this.testAcitvityNum > 1 && currentTime - this.latestTestActivityTime > 60) {
					//如果链路测试次数超过2次则直接重新连接网关
					if (this.testAcitvityNum > this.config.getActiveTesCount() - 1) {
						//重新连网关
						this.reLogin();
					} else {
						//再次发送链路测试包
						ActiveTestPacket activeTestPacket = new ActiveTestPacket();
						this.sendDataQueue.add(activeTestPacket);
					}
				} else if (currentTime - this.latestTestActivityTime > this.config.getActiveTestInterval() && this.latestBusyTime <= this.latestTestActivityTime) {
					//如果离上一次链路测试时间有3分钟并且链路的最后活跃时间小于最后的链路测试时间则发送一次链路测试
					logger.info("the last busy time is too long send activite test");
					ActiveTestPacket activeTestPacket = new ActiveTestPacket();
					this.sendDataQueue.add(activeTestPacket);
				} else {
					logger.info("no data to send sleep 10 secs");
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						logger.error("processData error", e);
					}
				}
			}
		}
	}
	
	
	private int processSendDataList() throws IOException {
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
					logger.info("send activity test, serial is " + data.getSequenceId());
				} else if (data.getRequestId() == MsgCommand.CMPP_TERMINATE) {
					//如果是退出包,则停止发送信息的任务，只接收信息
					logger.info("this is exit packet");
					this.stopSendData = true;
				}
			}
			//更新最后活跃时间
			this.latestBusyTime = System.currentTimeMillis();
		} else {
			logger.info("send dataList is empty");
		}
		return dataList.size();
	}
	
	private void reLogin() throws IOException {
		this.stopProcessData = true;
		this.stopReceiveData = true;
		this.stopSendData = true;
		if (this.msgSocket != null && !this.msgSocket.isClosed()) {
			this.msgSocket.close();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			logger.error("reLogin error", e);
		}
		this.init();
		
	}
	
	private class ReciveDataTask implements Runnable {

		@Override
		public void run() {
			try {
				Kernel.this.receiveData();
			} catch (IOException e) {
				logger.error("ReciveDataTask error", e);
				Kernel.this.stopReceiveData = true;
			}

		}

	}

	private class SendDataTask implements Runnable {

		@Override
		public void run() {
			
				try {
					Kernel.this.sendData();
				} catch (IOException e) {
					logger.error("SendDataTask error", e);
					Kernel.this.stopSendData = true;
				}

		}

	}
	
	private class ProcessDataTask implements Runnable {

		@Override
		public void run() {
			try {
				Kernel.this.processData();
			} catch (IOException e) {
				logger.error("ProcessDataTask error", e);
				Kernel.this.stopProcessData = true;
			}
			
		}
		
	}
}
