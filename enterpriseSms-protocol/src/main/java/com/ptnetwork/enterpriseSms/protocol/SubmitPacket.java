/**
 * Project Name:enterpriseSms-protocol
 * File Name:SubmitPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����10:02:34
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * ClassName:SubmitPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 ����10:02:34 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class SubmitPacket extends AbstractPacket {
	public final static String CONTENT_CHARSET = "utf-8";
	public final static int REQUEST_ID = MsgCommand.CMPP_SUBMIT;
	
	//�̶��ĳ��ȣ���ȥ��ȷ���Ķ������ݳ��Ⱥ�DestTermID��21*DestTermCount��
	//12(��ͷ����) + 1(MsgType) + 1(NeedReport) +1(Priority) + 10(ServiceID) + 2(FeeType)
	//+6(FixedFee) + 6(FeeCode) + 1(MsgFormat) + 17(ValidTime) + 17(AtTime)
	//+21(SrcTermID) + 21(ChargeTermID) + 1(DestTermIDCount) + 1��MsgLength��
	//+8(Reserve)
	public final static int REGUAL_PACKET_LENGTH = 126;
	
	private byte msgType;//��������
	private byte needReport;//�Ƿ�Ҫ�󷵻�״̬����,0��Ҫ��,1Ҫ��.
	private byte priority;//�������ȼ�.
	private String serviceId;//ҵ������(����Ϊ��).ԭ4ΪITSM,�Ƿ���Ҫ�͵���Э��?
	private String feeType;//�շ�����.ԭ4������ض�Ϊ01(����),Ӧ��Ϊ00(���)??
	private String fixedFee;//����(�ⶥ)��,ԭ4�������Ϊ000000.
	private String feeCode;//�ʷѴ���,ԭ4�������Ϊ000010
	private byte msgFormat;//����Ϣ��ʽ,Ĭ��Ϊ15
	private String validTime;//��Чʱ��,���ϵͳĬ��Ϊ48Сʱ
	private String atTime;//��ʱ����ʱ��,���Ĭ��Ϊb������
	private String srcTermId;//����Ϣ�����û�����.
	private String chargeTermId;//�Ʒ��û�����,ԭ4�������Ϊ05911110,�Ƿ���Ҫ����Э��??
	private byte destTermIdCount;//����Ϣ���պ�������.
	private String destTermId;//����Ϣ���պ���.
	private byte msgLength;//����Ϣ����
	private String msgContent;//����Ϣ����
	private String reserve;//����
	
    public SubmitPacket() {
    	super(REQUEST_ID);
	}
	
	public SubmitPacket(PacketHead packetHead) {
		super(packetHead);
	}

	public byte getMsgType() {
		return msgType;
	}

	public void setMsgType(byte msgType) {
		this.msgType = msgType;
	}

	public byte getNeedReport() {
		return needReport;
	}

	public void setNeedReport(byte needReport) {
		this.needReport = needReport;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFixedFee() {
		return fixedFee;
	}

	public void setFixedFee(String fixedFee) {
		this.fixedFee = fixedFee;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public byte getMsgFormat() {
		return msgFormat;
	}

	public void setMsgFormat(byte msgFormat) {
		this.msgFormat = msgFormat;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getAtTime() {
		return atTime;
	}

	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}

	public String getSrcTermId() {
		return srcTermId;
	}

	public void setSrcTermId(String srcTermId) {
		this.srcTermId = srcTermId;
	}

	public String getChargeTermId() {
		return chargeTermId;
	}

	public void setChargeTermId(String chargeTermId) {
		this.chargeTermId = chargeTermId;
	}

	public byte getDestTermIdCount() {
		return destTermIdCount;
	}

	public void setDestTermIdCount(byte destTermIdCount) {
		this.destTermIdCount = destTermIdCount;
	}

	public String getDestTermId() {
		return destTermId;
	}

	public void setDestTermId(String destTermId) {
		this.destTermId = destTermId;
	}

	public byte getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(byte msgLength) {
		this.msgLength = msgLength;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	
	public int getSequenceId() {
		return super.packetHead.getSequenceId();
	}
	
	public void setSequenceId(int sequenceId) {
		super.packetHead.setSequenceId(sequenceId);
	}

	@Override
	public byte[] encode() {
		//�������ܳ���
		byte[] contentBytes = null;
		try {
			contentBytes = this.msgContent.getBytes(CONTENT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (contentBytes != null) {
			this.msgLength = (byte)contentBytes.length;
		}
		//����ܳ��ȵ��� �̶�����+��Ϣ���ݳ���+Ŀ�����ĳ���
		super.packetHead.setPacketLength(REGUAL_PACKET_LENGTH + this.msgLength + (21 * this.destTermIdCount));
		byte[] data = new byte[super.packetHead.getPacketLength()];
		super.encodePacketHead(data);
		data[12] = this.msgType;
		data[13] = this.needReport;
		data[14] = this.priority;
		Utils.string2bytes(this.serviceId, data, 15, 10);
		Utils.string2bytes(this.feeType, data, 25, 2);
		Utils.string2bytes(this.fixedFee, data, 27, 6);
		Utils.string2bytes(this.feeCode, data, 33, 6);
		data[39] = this.msgFormat;
		int tempStart = 0;
		Utils.string2bytes(this.validTime, data, 40, 17);
		Utils.string2bytes(this.atTime, data, 57, 17);
		Utils.string2bytes(this.srcTermId, data, 74, 21);
		Utils.string2bytes(this.chargeTermId, data, 95, 21);
		data[116] = this.destTermIdCount;
		Utils.string2bytes(this.destTermId, data, 117, 21*this.destTermIdCount);
		data[117 + 21*this.destTermIdCount] = this.msgLength;
		tempStart = 117 + 21*this.destTermIdCount + 1;
		for (int i=0; i<this.msgLength; i++) {
			data[tempStart++] = contentBytes[i];
		}
//		Utils.string2bytes(this.msgContent, data, 117 + 21*this.destTermIdCount + 1, this.msgLength);
		Utils.string2bytes(this.reserve, data, 117 + 21*this.destTermIdCount + 1 + this.msgLength, 8);
		return data;
	}

	@Override
	public void decode(InputStream in) throws IOException {
		
		this.msgType = (byte)in.read();
		this.needReport = (byte)in.read();
		this.priority = (byte)in.read();
		this.serviceId = Utils.bytes2string(in, 10);
		this.feeType = Utils.bytes2string(in, 2);
		this.fixedFee = Utils.bytes2string(in, 6);
		this.feeCode = Utils.bytes2string(in, 6);
		this.msgFormat = (byte)in.read();
		this.validTime = Utils.bytes2string(in, 17);
		this.atTime = Utils.bytes2string(in, 17);
		this.srcTermId = Utils.bytes2string(in, 21);
		this.chargeTermId = Utils.bytes2string(in, 21);
		this.destTermIdCount = (byte)in.read();
		this.destTermId = Utils.bytes2string(in, 21*destTermIdCount);
		this.msgLength = (byte)in.read();
		this.msgContent = Utils.bytes2string(in, this.msgLength, CONTENT_CHARSET);
		this.reserve = Utils.bytes2string(in, 8);
		
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

