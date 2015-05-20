/**
 * Project Name:enterpriseSms-protocol
 * File Name:SubmitPacketSocketTest.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-27下午9:31:09
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import junit.framework.TestCase;

/**
 * ClassName:SubmitPacketSocketTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-27 下午9:31:09 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class SubmitPacketSocketTest extends TestCase {

	private Socket socket;

	protected void setUp() throws Exception {
		super.setUp();
		this.socket = new Socket("211.143.170.161", 8855);
		this.socket.setKeepAlive(true);
		this.socket.setSoTimeout(10000);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (this.socket != null) {
			this.socket.close();
		}
	}
	
	public void testSubmit() throws Exception {
		InputStream in = this.socket.getInputStream();
		OutputStream out = this.socket.getOutputStream();
		LoginPacket loginPacket = new LoginPacket();
		loginPacket.setClientId("413070");
		loginPacket.setVersion((byte)0x20);
		loginPacket.setTimestamp(Utils.getTimestamp());
		loginPacket.encodeAuthClient("Lf4GFYev");
		byte[] bs = loginPacket.encode();
		out.write(bs);
		int packetLength = Utils.bytes2int(in);
		super.assertEquals(true, packetLength > 0);
		byte[] data = new byte[packetLength];
		Utils.int2bytes(packetLength, data, 0);
		in.read(data, 4, packetLength - 4);
		PacketHead packetHead = new PacketHead();
		packetHead.setPacketLength(packetLength);
		packetHead.decode(data);
		assertEquals(MsgCommand.CMPP_CONNECT_RESP, packetHead.getRequestId());
		LoginRespPacket loginRespPacket = new LoginRespPacket(packetHead);
		assertEquals(0x00, loginRespPacket.getStatus());
		//assertNotNull(loginRespPacket.decodeAuthServer());
		SubmitPacket submitPacket = new SubmitPacket();
		submitPacket.setSequenceId(SequenceGenerator.getInstance().getSequence());
		submitPacket.setSrcId("");
		submitPacket.setFeeUserType((byte)0x02);
		submitPacket.setMsgSrc("413070");
		submitPacket.setSrcId("106575322484");
		submitPacket.setDestUsrTl((byte)0x01);
		submitPacket.setDestTerminalId("13950079348");
		submitPacket.setMsgLength((byte)12);
		submitPacket.setMsgContent("111111111111");
//		submitPacket.setMsgType((byte)0x00);
//		submitPacket.setNeedReport((byte)0x00);
//		submitPacket.setPriority((byte)0x00);
//		submitPacket.setServiceId("12345");
//		submitPacket.setFeeType("2");
//		submitPacket.setFixedFee("123");
//		submitPacket.setFeeCode("123");
//		submitPacket.setMsgFormat((byte)0x15);
//		submitPacket.setSrcTermId("139500079348");
//		submitPacket.setChargeTermId("139500079348");
//		submitPacket.setDestTermId("139500079348");
//		submitPacket.setDestTermIdCount((byte)0x01);
		out.write(submitPacket.encode());
		packetLength = Utils.bytes2int(in);
		super.assertEquals(true, packetLength > 0);
		data = new byte[packetLength];
		Utils.int2bytes(packetLength, data, 0);
		in.read(data, 4, packetLength - 4);
		packetHead = new PacketHead();
		packetHead.setPacketLength(packetLength);
		packetHead.decode(data);
		assertEquals(MsgCommand.CMPP_SUBMIT_RESP, packetHead.getRequestId());
		SubmitRespPacket submitRespPacket = new SubmitRespPacket(packetHead);
		assertEquals(0x00, submitRespPacket.getStatus());
		in.close();
		out.close();
	}

}

