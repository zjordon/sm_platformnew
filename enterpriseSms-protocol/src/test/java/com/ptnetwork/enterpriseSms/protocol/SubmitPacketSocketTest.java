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
		this.socket = new Socket("localhost", 3001);
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
		loginPacket.setClientId("test");
		loginPacket.setLoginMode((byte)0x00);
		loginPacket.encodeAuthClient("test123");
		out.write(loginPacket.encode());
		PacketHead packetHead = new PacketHead();
		packetHead.decode(in);
		assertEquals(0x80000001, packetHead.getRequestId());
		LoginRespPacket loginRespPacket = new LoginRespPacket(packetHead);
		loginRespPacket.decode(in);
		assertEquals(0x00, loginRespPacket.getStatus());
		assertNotNull(loginRespPacket.decodeAuthServer());
		SubmitPacket submitPacket = new SubmitPacket();
		submitPacket.setMsgType((byte)0x00);
		submitPacket.setNeedReport((byte)0x00);
		submitPacket.setPriority((byte)0x00);
		submitPacket.setServiceId("12345");
		submitPacket.setFeeType("2");
		submitPacket.setFixedFee("123");
		submitPacket.setFeeCode("123");
		submitPacket.setMsgFormat((byte)0x15);
		submitPacket.setSrcTermId("139500079348");
		submitPacket.setChargeTermId("139500079348");
		submitPacket.setDestTermId("139500079348");
		submitPacket.setDestTermIdCount((byte)0x01);
		submitPacket.setMsgContent("fuckboss!");
		out.write(submitPacket.encode());
		packetHead.decode(in);
		assertEquals(0x80000002, packetHead.getRequestId());
		SubmitRespPacket submitRespPacket = new SubmitRespPacket(packetHead);
		submitRespPacket.decode(in);
		assertEquals(0x00, submitRespPacket.getStatus());
		in.close();
		out.close();
	}

}
