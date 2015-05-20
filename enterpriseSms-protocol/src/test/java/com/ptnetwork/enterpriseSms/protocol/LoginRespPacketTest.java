/**
 * Project Name:enterpriseSms-protocol
 * File Name:LoginRespPacketTest.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����9:27:22
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * ClassName:LoginRespPacketTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 ����9:27:22 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class LoginRespPacketTest extends TestCase {
	private LoginRespPacket loginRespPacket;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testEncode() {
		this.loginRespPacket = new LoginRespPacket(1);
		this.loginRespPacket.setStatus((byte)0);
//		this.loginRespPacket.setAuthenticatorServer("87654321");
		this.loginRespPacket.setVersion((byte)0x20);
		byte[] data = this.loginRespPacket.encode();
		assertNotNull(data);
		int tempStart = 0;
		int i = 0;
		byte[] expectedPacketLength = new byte[]{0x21, 0x00, 0x00, 0x00};//33
		for (i=0; i<4; i++) {
			assertEquals(expectedPacketLength[i], data[tempStart++]);
		}
		byte[] expectedRequestId = new byte[]{0x01, 0x00, 0x00, (byte)0x80};//0x80000001
		tempStart = 4;
		for (i=0; i<4; i++) {
			assertEquals(expectedRequestId[i], data[tempStart++]);
		}
		byte[] exceptedStatus = new byte[]{0x00,0x00,0x00,0x00};
		tempStart = 12;
		for (i=0; i<4; i++) {
			assertEquals(exceptedStatus[i], data[tempStart++]);
		}
		byte[] expectedAuthServer = new byte[] {0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31,
				0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		tempStart = 16;
		for (i=0; i<16; i++) {
			assertEquals(expectedAuthServer[i], data[tempStart++]);
		}
		assertEquals(0x20, data[32]);
	}

	public void testDecode() {
		byte[] data = new byte[33];
		int destStart = 0;
		byte[] packetLength = new byte[]{0x21, 0x00, 0x00, 0x00};//42
		destStart = Utils.copyBytes(packetLength, data, destStart);
		byte[] requestId = new byte[]{0x01, 0x00, 0x00, (byte)0x80};//0x80000001
		destStart = Utils.copyBytes(requestId, data, destStart);
		byte[] sequenceId = new byte[]{0x01, 0x00, 0x00, 0x00};//0x00000001
		destStart = Utils.copyBytes(sequenceId, data, destStart);
		byte[] status = new byte[]{0x00,0x00,0x00,0x00};
		destStart = Utils.copyBytes(status, data, destStart);
		byte[] authServer = new byte[] {0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31,
				0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		destStart = Utils.copyBytes(authServer, data, destStart);
		data[destStart] = 0x20;
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		PacketHead packetHead = new PacketHead();
		Exception exception = null;
		try {
			packetHead.decode(in);
		} catch (IOException e) {
			e.printStackTrace();
			exception = e;
		}
		if (exception == null) {
			assertEquals(33, packetHead.getPacketLength());
			assertEquals(0x80000001, packetHead.getRequestId());
			assertEquals(0x00000001, packetHead.getSequenceId());
			loginRespPacket = new LoginRespPacket(packetHead);
			try {
				loginRespPacket.decode(in);
			} catch (IOException e) {
				
				exception = e;
				e.printStackTrace();
				
			}
			
			if (exception == null) {
				assertEquals(0, loginRespPacket.getStatus());
				assertEquals(0x20, loginRespPacket.getVersion());
			}
			
		}
	}

}

