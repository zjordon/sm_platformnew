/**
 * 
 */
package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import junit.framework.TestCase;

/**
 * @author jasonzhang
 *
 */
public class DeliverPacketSocketTest extends TestCase {
	
	private Socket socket;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.socket = new Socket("211.143.170.161", 8855);
		this.socket.setKeepAlive(true);
		this.socket.setSoTimeout(300000);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		if (this.socket != null) {
			this.socket.close();
		}
	}
	/*
	public void testDecode() throws IOException {
		InputStream in = this.socket.getInputStream();
		OutputStream out = this.socket.getOutputStream();
		//先登录
		LoginPacket loginPacket = new LoginPacket();
		loginPacket.setClientId("413070");
		loginPacket.setVersion((byte)0x20);
		loginPacket.setTimestamp(Utils.getTimestamp());
		loginPacket.encodeAuthClient("Lf4GFYev");
		byte[] bs = loginPacket.encode();
		out.write(bs);
		out.flush();
		PacketHead packetHead = new PacketHead();
		packetHead.decode(in);
		assertEquals(0x80000001, packetHead.getRequestId());
		LoginRespPacket loginRespPacket = new LoginRespPacket(packetHead);
		loginRespPacket.decode(in);
		if (loginRespPacket.getStatus() == 0) {
			//登录成功
			//等待5分钟左右，这时用手机发送一条短信到106575322484进行测试
			long currentTime = System.currentTimeMillis();
			long endTime = currentTime + 5 * 60 * 1000;
			while (currentTime < endTime) {
				packetHead.decode(in);
				switch (packetHead.getRequestId()) {
				case MsgCommand.CMPP_DELIVER:
					System.out.println("短信来了");
					DeliverPacket deliverPacket = new DeliverPacket();
					deliverPacket.decode(in);
					System.out.println("发送号码为:" + deliverPacket.getSrcTermId() + ",消息内容为:" + deliverPacket.getMsgContent());
					DeliverRespPacket deliverRespPacket = new DeliverRespPacket(deliverPacket.getSequenceId());
					deliverRespPacket.setMsgId(deliverPacket.getMsgId());
					deliverRespPacket.setStatus((byte)0);
					out.write(deliverRespPacket.encode());
					out.flush();
					break;
				default:
					System.out.println("其它类型的消息，命令格式为:" + packetHead.getRequestId());
					break;
				}
				currentTime = System.currentTimeMillis();
			}
		}
	}
	*/
	public void testDecodeBytes() throws IOException {
		InputStream in = this.socket.getInputStream();
		OutputStream out = this.socket.getOutputStream();
		//先登录
		LoginPacket loginPacket = new LoginPacket();
		loginPacket.setClientId("413070");
		loginPacket.setVersion((byte)0x20);
		loginPacket.setTimestamp(Utils.getTimestamp());
		loginPacket.encodeAuthClient("Lf4GFYev");
		byte[] bs = loginPacket.encode();
//		for (int i=0; i<bs.length; i++) {
//			System.out.println(bs[i]);
//		}
		//out.write(loginPacket.encode());
		out.write(bs);
		out.flush();
		int packetLength = Utils.bytes2int(in);
		super.assertEquals(true, packetLength > 0);
		//System.out.println("packetLength is " + packetLength);
		byte[] data = new byte[packetLength];
		Utils.int2bytes(packetLength, data, 0);
		in.read(data, 4, packetLength - 4);
		PacketHead packetHead = new PacketHead();
		packetHead.setPacketLength(packetLength);
		packetHead.decode(data);
		assertEquals(MsgCommand.CMPP_CONNECT_RESP, packetHead.getRequestId());
		LoginRespPacket loginRespPacket = new LoginRespPacket(packetHead);
		//loginRespPacket.decode(in);
		loginRespPacket.decode(data);
		if (loginRespPacket.getStatus() == 0) {
			//登录成功
			//等待5分钟左右，这时用手机发送一条短信到106575322484进行测试
			long currentTime = System.currentTimeMillis();
			long endTime = currentTime + 5 * 60 * 1000;
			while (currentTime < endTime) {
				packetLength = Utils.bytes2int(in);
				data = new byte[packetLength];
				Utils.int2bytes(packetLength, data, 0);
				in.read(data, 4, packetLength - 4);
				packetHead.setPacketLength(packetLength);
				packetHead.decode(data);
				switch (packetHead.getRequestId()) {
				case MsgCommand.CMPP_DELIVER:
					System.out.println("短信来了");
					DeliverPacket deliverPacket = new DeliverPacket();
					deliverPacket.decode(data);
					System.out.println("发送号码为:" + deliverPacket.getSrcTermId() + ",消息内容为:" + deliverPacket.getMsgContent());
					DeliverRespPacket deliverRespPacket = new DeliverRespPacket(deliverPacket.getSequenceId());
					deliverRespPacket.setMsgId(deliverPacket.getMsgId());
					deliverRespPacket.setStatus((byte)0);
					out.write(deliverRespPacket.encode());
					out.flush();
					break;
				default:
					System.out.println("其它类型的消息，命令格式为:" + packetHead.getRequestId());
					break;
				}
				currentTime = System.currentTimeMillis();
			}
		}
	}

}
