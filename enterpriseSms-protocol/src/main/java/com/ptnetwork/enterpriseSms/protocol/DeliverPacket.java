/**
 * Project Name:enterpriseSms-protocol
 * File Name:DeliverPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����4:29:04
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * ClassName:DeliverPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2013-1-24 ����4:29:04 <br/>
 * 
 * @author JasonZhang
 * @version
 * @since JDK 1.6
 * @see
 */
public class DeliverPacket extends AbstractPacket {
	public final static String CONTENT_CHARSET = "utf-8";
	public final static int REQUEST_ID = MsgCommand.CMPP_DELIVER;

	// �̶��ĳ��ȣ���ȥ��ȷ���Ķ������ݳ���
	// 12(消息头) + 8(MsgID) + 21(DestTermID)  + 10(Service_Id) + 1(TP_pid) + 1(TP_udhi) +1(MsgFormat) +21(SrcTermID) 
	// 1(Registered_Delivery) + 1(MsgLength) + 实际的短信内容的长度  +8(Reserve) 
	public final static int REGUAL_PACKET_LENGTH = 85;

	private long msgId;//信息标识
	private String destTermId;//目的号码
	private String serviceId;//业务类型
	private byte tpPid;//GSM协议类型
	private byte tpUdhi;//GSM协议类型
	private byte msgFormat;//信息格式
	private String srcTermId;//源终端MSISDN号码
	private byte isReport;//是否为状态报告
	private byte msgLength;//消息长度
	private String msgContent;//消息内容
	private String reserve;//保留项
	
	private StatusReport statusReport;

	public DeliverPacket() {
		super(REQUEST_ID);
	}

	public DeliverPacket(PacketHead packetHead) {

		super(packetHead);

	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public byte getIsReport() {
		return isReport;
	}

	public void setIsReport(byte isReport) {
		this.isReport = isReport;
	}

	public byte getMsgFormat() {
		return msgFormat;
	}

	public void setMsgFormat(byte msgFormat) {
		this.msgFormat = msgFormat;
	}

	

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getSrcTermId() {
		return srcTermId;
	}

	public void setSrcTermId(String srcTermId) {
		this.srcTermId = srcTermId;
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

	@Override
	public byte[] encode() {

		// �������ܳ���
		byte[] contentBytes = null;
		try {
			contentBytes = this.msgContent.getBytes(CONTENT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (contentBytes != null) {
			this.msgLength = (byte) contentBytes.length;
		}
		// ����ܳ��ȵ��� �̶�����+��Ϣ���ݳ���
		super.packetHead.setPacketLength(REGUAL_PACKET_LENGTH + this.msgLength);
		byte[] data = new byte[super.packetHead.getPacketLength()];
		super.encodePacketHead(data);
		//Utils.int2bytes(this.msgId, data, 12);
		data[12] = this.isReport;
		data[13] = this.msgFormat;
		//Utils.string2bytes(this.recvTime, data, 14, 14);
		Utils.string2bytes(this.srcTermId, data, 28, 21);
		Utils.string2bytes(this.destTermId, data, 49, 21);
		data[70] = this.msgLength;
		int tempStart = 71;
		for (int i=0; i<this.msgLength; i++) {
			data[tempStart++] = contentBytes[i];
		}
		if (this.reserve == null) {
			tempStart = 71 + this.msgLength;
			for (int i=0; i<8; i++) {
				data[tempStart++] = (byte) 0x00;
			}
		} else {
			Utils.string2bytes(this.reserve, data, 71 + this.msgLength, 8);
		}
		return data;
	}

	@Override
	public void decode(InputStream in) throws IOException {

		this.msgId = Utils.bytes2long(in);
		this.destTermId = Utils.bytes2string(in, 21);
		this.serviceId = Utils.bytes2string(in, 10);
		this.tpPid = (byte)in.read();
		this.tpUdhi = (byte)in.read();
		this.msgFormat = (byte)in.read();
		this.srcTermId = Utils.bytes2string(in, 21);
		this.isReport = (byte)in.read();
		this.msgLength = (byte)in.read();
		if (this.isReport == 1) {
			//如果是状态报告，则解析
			this.statusReport = new StatusReport();
			this.statusReport.decode(in);
		} else {
			String fmtStr = "GB2312";
			switch (this.msgFormat) {
			case 0 :
				fmtStr = "ASCII";
				break;
			case 8:
				fmtStr = "UnicodeBigUnmarked";
				break;
			default :
				fmtStr = "GB2312";
				break;
			}
			this.msgContent = Utils.bytes2string(in, this.msgLength, fmtStr);
		}
		this.reserve = Utils.bytes2string(in, 8);

	}
	
	public void decode(byte[] data) throws IOException {
		this.msgId = Utils.bytes2long(data, 12);
		this.destTermId = Utils.bytes2string(data, 20, 21);
		this.serviceId = Utils.bytes2string(data, 41, 10);
		this.tpPid = data[51];
		this.tpUdhi = data[52];
		this.msgFormat = data[53];
		this.srcTermId = Utils.bytes2string(data, 54, 21);
		this.isReport = data[75];
		this.msgLength =  data[76];
		if (this.isReport == 1) {
			//如果是状态报告，则解析
			this.statusReport = new StatusReport();
			this.statusReport.decode(data, 77);
			//状态报告的长度为60
			this.reserve = Utils.bytes2string(data, 137, 8);
		} else {
			String fmtStr = "GB2312";
			switch (this.msgFormat) {
			case 0 :
				fmtStr = "ASCII";
				break;
			case 8:
				fmtStr = "UnicodeBigUnmarked";
				break;
			default :
				fmtStr = "GB2312";
				break;
			}
			this.msgContent = Utils.bytes2string(data, 77, this.msgLength, fmtStr);
			this.reserve = Utils.bytes2string(data, (77+this.msgLength), 8);
		}
		
	}

	@Override
	public int getRequestId() {
		return REQUEST_ID;
	}

}
