/**
 * Project Name:enterpriseSms-protocol
 * File Name:LoginPacketSocketTest.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-27下午9:00:10
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import junit.framework.TestCase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ClassName:LoginPacketSocketTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-27 下午9:00:10 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class LoginPacketSocketTest extends TestCase {
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
	/*
	public void testLogin() throws Exception {
		InputStream in = this.socket.getInputStream();
		OutputStream out = this.socket.getOutputStream();
		//DataInputStream in = new DataInputStream(this.socket.getInputStream());
		//DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
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
		PacketHead packetHead = new PacketHead();
		packetHead.decode(in);
		assertEquals(0x80000001, packetHead.getRequestId());
		LoginRespPacket loginRespPacket = new LoginRespPacket(packetHead);
		loginRespPacket.decode(in);
		assertEquals(0x00, loginRespPacket.getStatus());
		assertNotNull(loginRespPacket.decodeAuthServer());
//		 int len=in.readInt();
//		 if(null!=in&& len > 0){
//		   byte[] data=new byte[len-4];
//	   	   in.read(data);
//	   	   System.out.println(data);
//		 }
		
		in.close();
		out.close();
	}
	*/
	public void testLoginWithBytes() throws Exception {
		InputStream in = this.socket.getInputStream();
		OutputStream out = this.socket.getOutputStream();
		//DataInputStream in = new DataInputStream(this.socket.getInputStream());
		//DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
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
		assertEquals(0x00, loginRespPacket.getStatus());
		assertNotNull(loginRespPacket.decodeAuthServer());
//		 int len=in.readInt();
//		 if(null!=in&& len > 0){
//		   byte[] data=new byte[len-4];
//	   	   in.read(data);
//	   	   System.out.println(data);
//		 }
		
		in.close();
		out.close();
	}

}

