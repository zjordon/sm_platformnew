/**
 * Project Name:enterpriseSms-protocol
 * File Name:SubmitRespPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����5:35:55
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * ClassName:SubmitRespPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 ����5:35:55 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class SubmitRespPacket extends AbstractPacket {

	public final static int REQUEST_ID = MsgCommand.CMPP_SUBMIT_RESP;

	// ��ĳ���
	// 12(��ͷ����) + 10(MsgID) + 4(Status)
	public final static int PACKET_LENGTH = 26;
	
	private String msgId;//��ز���Ķ���Ϣ��ˮ�ţ��������ɣ���ش��룺3�ֽڣ�BCD�룩ʱ�䣺4�ֽڣ�BCD�룩���кţ�3�ֽڣ�BCD�룩

	private int status;//Submit���󷵻ؽ��

	public SubmitRespPacket() {
		super(PACKET_LENGTH, REQUEST_ID);
	}

	public SubmitRespPacket(PacketHead packetHead) {
		super(packetHead);
	}
	
	public SubmitRespPacket(int sequenceId) {
		super(PACKET_LENGTH, REQUEST_ID);
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public byte[] encode() {
		
		byte[] data = new byte[PACKET_LENGTH];
		super.encodePacketHead(data);
		Utils.string2bytes(this.msgId, data, 12, 10);
		Utils.int2bytes(this.status, data, 22);
		return data;
	}

	@Override
	public void decode(InputStream in) throws IOException {
		
		this.msgId = Utils.bytes2string(in, 10);
		this.status = Utils.bytes2int(in);
		
		
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

