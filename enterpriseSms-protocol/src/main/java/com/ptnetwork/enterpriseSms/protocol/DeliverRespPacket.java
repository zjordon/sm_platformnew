/**
 * Project Name:enterpriseSms-protocol
 * File Name:DeliverRespPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����5:41:53
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * SMGW��SP���Ͷ���Ϣ�Ļ�Ӧ
 * ClassName:DeliverRespPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 ����5:41:53 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class DeliverRespPacket extends AbstractPacket {

	public final static int REQUEST_ID = MsgCommand.CMPP_DELIVER_RESP;

	// ��ĳ���
	// 12(��ͷ����) + 8(MsgID) + 1(Status)
	public final static int PACKET_LENGTH = 21;
	
	private long msgId;//

	private byte status;//Submit���󷵻ؽ��

	public DeliverRespPacket(int sequenceId) {
		super(PACKET_LENGTH, REQUEST_ID, sequenceId);
	}

	public DeliverRespPacket(PacketHead packetHead) {
		super(packetHead);
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Override
	public byte[] encode() {
		
		byte[] data = new byte[PACKET_LENGTH];
		super.encodePacketHead(data);
		Utils.longtobytes(this.msgId, data, 12);
		this.status = data[20];
		return data;
	}

	@Override
	public void decode(InputStream in) throws IOException {
		
		this.msgId = Utils.bytes2long(in);
		this.status = (byte)in.read();
		
		
	}

	@Override
	public void decode(byte[] data) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRequestId() {
		return REQUEST_ID;
	}
}

