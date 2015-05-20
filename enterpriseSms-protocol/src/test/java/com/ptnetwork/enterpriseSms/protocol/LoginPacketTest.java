/**
 * Project Name:enterpriseSms-protocol
 * File Name:LoginPacketTest.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24обнГ5:46:53
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * ClassName:LoginPacketTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 обнГ5:46:53 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class LoginPacketTest extends TestCase {
	private LoginPacket loginPacket;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testEncode() {
		loginPacket = new LoginPacket();
		loginPacket.setClientId("12345678");
//		loginPacket.se("87654321");
		loginPacket.setLoginMode((byte)1);
		loginPacket.setTimestamp(301000000);
		loginPacket.setVersion((byte)0x20);
		byte[] data = loginPacket.encode();
		assertNotNull(data);
		int tempStart = 0;
		int i = 0;
		byte[] expectedPacketLength = new byte[]{0x2A, 0x00, 0x00, 0x00};//42
		for (i=0; i<4; i++) {
			assertEquals(expectedPacketLength[i], data[tempStart++]);
		}
		byte[] expectedRequestId = new byte[]{0x01, 0x00, 0x00, 0x00};//0x00000001
		tempStart = 4;
		for (i=0; i<4; i++) {
			assertEquals(expectedRequestId[i], data[tempStart++]);
		}
		
		byte[] expectedClientId = new byte[] {0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38};
		tempStart = 12;
		for (i=0; i<8; i++) {
			assertEquals(expectedClientId[i], data[tempStart++]);
		}
		
		byte[] expectedAuthClient = new byte[] {0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31,
				0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		tempStart = 20;
		for (i=0; i<16; i++) {
			assertEquals(expectedAuthClient[i], data[tempStart++]);
		}
		tempStart = 36;
		byte expectedLoginMode = (byte)0x01;
		assertEquals(expectedLoginMode, data[tempStart]);
		
		byte[] expectedTimestamp = new byte[] {0x40, (byte)0xE5, (byte)0xF0, 0x11};
		tempStart = 37;
		for (i=0; i<4; i++) {
			assertEquals(expectedTimestamp[i], data[tempStart++]);
		}
		
		tempStart = 41;
		byte expectedVersion = (byte)0x20;
		assertEquals(expectedVersion, data[tempStart]);
		
	}

	public void testDecode() {
		byte[] data = new byte[42];
		int destStart = 0;
		byte[] packetLength = new byte[]{0x2A, 0x00, 0x00, 0x00};//42
		destStart = Utils.copyBytes(packetLength, data, destStart);
		byte[] requestId = new byte[]{0x01, 0x00, 0x00, 0x00};//0x00000001
		destStart = Utils.copyBytes(requestId, data, destStart);
		byte[] sequenceId = new byte[]{0x01, 0x00, 0x00, 0x00};//0x00000001
		destStart = Utils.copyBytes(sequenceId, data, destStart);
		byte[] clientId = new byte[] {0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38};
		destStart = Utils.copyBytes(clientId, data, destStart);
		byte[] authClient = new byte[] {0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31,
				0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		destStart = Utils.copyBytes(authClient, data, destStart);
		byte loginMode = (byte)0x01;
		data[destStart++] = loginMode;
		byte[] timestamp = new byte[] {0x40, (byte)0xE5, (byte)0xF0, 0x11};
		destStart = Utils.copyBytes(timestamp, data, destStart);
		byte version = (byte)0x20;
		data[destStart] = version;
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
			assertEquals(42, packetHead.getPacketLength());
			assertEquals(0x00000001, packetHead.getRequestId());
			assertEquals(0x00000001, packetHead.getSequenceId());
			loginPacket = new LoginPacket(packetHead);
			try {
				loginPacket.decode(in);
			} catch (IOException e) {
				
				exception = e;
				e.printStackTrace();
				
			}
			
			if (exception == null) {
				assertEquals("12345678", loginPacket.getClientId());
//				assertEquals("87654321", loginPacket.getSharedsecret());
				assertEquals(0x01, loginPacket.getLoginMode());
				assertEquals(301000000, loginPacket.getTimestamp());
				assertEquals(0x20, loginPacket.getVersion());
			}
			
		}
	}

}

