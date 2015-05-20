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
	public final static String CONTENT_CHARSET = "gb2312";
	public final static int REQUEST_ID = MsgCommand.CMPP_SUBMIT;
	
	
	//12(头部) + 8(Msg_Id) + 1(Pk_total) + 1(Pk_number) + 1(Registered_Delivery) +1(Msg_level) + 10(ServiceID) + 1(Fee_UserType)
	//+21(Fee_terminal_Id) + 1(TP_pId) + 1(TP_udhi) + 1(Msg_Fmt) + 6(Msg_src) + 2(FeeType) + 6(FeeCode)
	//+17(ValId_Time) + 17(At_Time) + 21(Src_Id) + 1(DestUsr_tl) + 21*DestUsr_tl(Dest_terminal_Id) + 1(Msg_Length) + Msg_Length(Msg_Content)
	//+8(Reserve)
	public final static int REGUAL_PACKET_LENGTH = 138;
	
	private long msgId = 0;//信息标识，由SP侧短信网关本身产生，本处填空。
	private byte pkTotal = 0x01;
	private byte pkNumber = 0x01;
	private byte needReport;
	private byte msgLevel;
	private String serviceId;
	private byte feeUserType;
	private String feeTerminalId;
	private byte tpPid;
	private byte tpUdhi;
	private byte msgFmt = 0x0f;
	private String msgSrc;
	private String feeType;
	private String feeCode;
	private String validTime;
	private String atTime;
	private String srcId;
	private byte destUsrTl;
	private String destTerminalId;
	private byte msgLength;
	private String msgContent;
	private String reserve;
	
    public SubmitPacket() {
    	super(REQUEST_ID);
	}
	
	public SubmitPacket(PacketHead packetHead) {
		super(packetHead);
	}
	
	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public byte getPkTotal() {
		return pkTotal;
	}

	public void setPkTotal(byte pkTotal) {
		this.pkTotal = pkTotal;
	}

	public byte getPkNumber() {
		return pkNumber;
	}

	public void setPkNumber(byte pkNumber) {
		this.pkNumber = pkNumber;
	}

	public byte getNeedReport() {
		return needReport;
	}

	public void setNeedReport(byte needReport) {
		this.needReport = needReport;
	}

	public byte getMsgLevel() {
		return msgLevel;
	}

	public void setMsgLevel(byte msgLevel) {
		this.msgLevel = msgLevel;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public byte getFeeUserType() {
		return feeUserType;
	}

	public void setFeeUserType(byte feeUserType) {
		this.feeUserType = feeUserType;
	}

	public String getFeeTerminalId() {
		return feeTerminalId;
	}

	public void setFeeTerminalId(String feeTerminalId) {
		this.feeTerminalId = feeTerminalId;
	}

	public byte getTpPid() {
		return tpPid;
	}

	public void setTpPid(byte tpPid) {
		this.tpPid = tpPid;
	}

	public byte getTpUdhi() {
		return tpUdhi;
	}

	public void setTpUdhi(byte tpUdhi) {
		this.tpUdhi = tpUdhi;
	}

	public byte getMsgFmt() {
		return msgFmt;
	}

	public void setMsgFmt(byte msgFmt) {
		this.msgFmt = msgFmt;
	}

	public String getMsgSrc() {
		return msgSrc;
	}

	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
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

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public byte getDestUsrTl() {
		return destUsrTl;
	}

	public void setDestUsrTl(byte destUsrTl) {
		this.destUsrTl = destUsrTl;
	}

	public String getDestTerminalId() {
		return destTerminalId;
	}

	public void setDestTerminalId(String destTerminalId) {
		this.destTerminalId = destTerminalId;
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
		
		byte[] contentBytes = null;
		try {
			contentBytes = this.msgContent.getBytes(CONTENT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (contentBytes != null) {
			this.msgLength = (byte)contentBytes.length;
		}
		
		super.packetHead.setPacketLength(REGUAL_PACKET_LENGTH + this.msgLength + (21 * this.destUsrTl));
		byte[] data = new byte[super.packetHead.getPacketLength()];
		super.encodePacketHead(data);
		Utils.longtobytes(this.msgId, data, 12);
		data[20] = this.pkTotal;
		data[21] = this.pkNumber;
		data[22] = this.needReport;
		data[23] = this.msgLevel;
		Utils.string2bytes(this.serviceId, data, 24, 10);
		data[34] = this.feeUserType;
		Utils.string2bytes(this.feeTerminalId != null ? this.feeTerminalId : "", data, 35, 21);
		data[56] = this.tpPid;
		data[57] = this.tpUdhi;
		data[58] = this.msgFmt;
		Utils.string2bytes(this.msgSrc, data, 59, 6);
		Utils.string2bytes(this.feeType, data, 65, 2);
		Utils.string2bytes(this.feeCode, data, 67, 6);
		Utils.string2bytes(this.validTime != null ? this.validTime : "", data, 73, 17);
		Utils.string2bytes(this.atTime != null ? this.atTime : "", data, 90, 17);
		Utils.string2bytes(this.srcId, data, 107, 21);
		data[128] = this.destUsrTl;
		Utils.string2bytes(this.destTerminalId, data, 129, 21*this.destUsrTl);
		data[129+21*destUsrTl] = this.msgLength;
		for (int i=0;i<contentBytes.length;i++) {
			data[129+21*destUsrTl + 1 +i] = contentBytes[i];
		}
		Utils.string2bytes(this.reserve != null ? this.reserve : "", data, 129+21*destUsrTl + 1 + contentBytes.length, 8);
		return data;
	}

	@Override
	public void decode(InputStream in) throws IOException {
		
//		this.msgType = (byte)in.read();
//		this.needReport = (byte)in.read();
//		this.priority = (byte)in.read();
//		this.serviceId = Utils.bytes2string(in, 10);
//		this.feeType = Utils.bytes2string(in, 2);
//		this.fixedFee = Utils.bytes2string(in, 6);
//		this.feeCode = Utils.bytes2string(in, 6);
//		this.msgFormat = (byte)in.read();
//		this.validTime = Utils.bytes2string(in, 17);
//		this.atTime = Utils.bytes2string(in, 17);
//		this.srcTermId = Utils.bytes2string(in, 21);
//		this.chargeTermId = Utils.bytes2string(in, 21);
//		this.destTermIdCount = (byte)in.read();
//		this.destTermId = Utils.bytes2string(in, 21*destTermIdCount);
//		this.msgLength = (byte)in.read();
//		this.msgContent = Utils.bytes2string(in, this.msgLength, CONTENT_CHARSET);
//		this.reserve = Utils.bytes2string(in, 8);
		
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

