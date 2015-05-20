/**
 * Project Name:enterpriseSms-protocol
 * File Name:PackageHead.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-23����10:11:04
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/
/**
 * Project Name:enterpriseSms-protocol
 * File Name:PackageHead.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-23����10:11:04
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * ��ݰ��ͷ
 * ClassName:PackageHead <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-23 ����10:11:04 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class PacketHead {

	private int packetLength;//��ݰ�ȣ���ͷ�Ͱ���ĳ���֮�͡���λ���ֽڣ�
	private int requestId;//�����ʶ
	private int sequenceId;//���кţ�������˷��䣬ѭ������ȡֵ0x00000000��0xFFFFFFFF��
	
	public PacketHead() {
	}
	public PacketHead(int requestId) {
		this.requestId = requestId;
		this.sequenceId = SequenceGenerator.getInstance().getSequence();
	}
	public PacketHead(int packetLength, int requestId) {
		this.packetLength = packetLength;
		this.requestId = requestId;
		this.sequenceId = SequenceGenerator.getInstance().getSequence();
	}
	public PacketHead(int packetLength, int requestId, int sequenceId) {
		this.packetLength = packetLength;
		this.requestId = requestId;
		this.sequenceId = sequenceId;
	}
	public int getPacketLength() {
		return packetLength;
	}
	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	public void encode(byte[] data) {
		Utils.int2bytes(this.packetLength, data, 0);
		Utils.int2bytes(this.requestId, data, 4);
		Utils.int2bytes(this.sequenceId, data, 8);
	}
	
	public void decode(InputStream in) throws IOException {
		//this.packetLength = Utils.bytes2int(in);
		this.requestId = Utils.bytes2int(in);
		this.sequenceId = Utils.bytes2int(in);
	}
	
	public void decode(byte[] data) throws IOException {
		this.requestId = Utils.bytes2int(data, 4);
		this.sequenceId = Utils.bytes2int(data, 8);
	}
}

