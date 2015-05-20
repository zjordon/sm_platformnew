/**
 * Project Name:enterpriseSms-protocol
 * File Name:Abstract.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-23����10:26:33
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * ������ݰ�ĸ���
 * ClassName:Abstract <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-23 ����10:26:33 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public abstract class AbstractPacket {

	protected PacketHead packetHead;
	
	protected int requestId;
	
	protected AbstractPacket(int packetLength, int requestId, int sequenceId){
		this.packetHead = new PacketHead(packetLength, requestId, sequenceId);
	}
	
	protected AbstractPacket(int packetLength, int requestId){
		this.packetHead = new PacketHead(packetLength, requestId);
	}
	
	protected AbstractPacket(int requestId){
		this.packetHead = new PacketHead(requestId);
	}
	
	protected AbstractPacket(PacketHead packetHead) {
		this.packetHead = packetHead;
	}
	
	public abstract byte[] encode();
	
	public abstract void decode(InputStream in) throws IOException ;
	public abstract void decode(byte[] data) throws IOException;
	
	protected void encodePacketHead(byte[] data) {
		this.packetHead.encode(data);
	}
	
	public int getSequenceId() {
		return this.packetHead.getSequenceId();
	}
	
	public abstract int getRequestId();
}

