/**
 * @(#) Deliver.java 2006-3-1
 * 
 * Copyright 2006 ptnetwork
 */
package com.ptnetwork.enterpriseSms.client.domain;

import java.util.Date;

/**
 * @author jasonzhang
 *
 */
public class Deliver extends MsgBase {
	public static final int STATE_SENDING = 0;
	public static final int STATE_SEND_FAIL = 1;

	private String smmsgContent;//接收消息的消息内容。
	private long msgId;//接收消息id（唯一标识），为-1的时候表明此消息为无效消息。
	private String recvTime;//����Ϣ����ʱ��
	private Date createDate;
	private String serviceId;
	private boolean isReport;

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(String recvTime) {
		this.recvTime = recvTime;
	}
	
	public String toString() {
		StringBuffer builder = new StringBuffer();
		builder.append("id:").append(this.getId()).append(' ');
		builder.append("msgId:").append(this.getMsgId()).append(' ');
		builder.append("msgFormat:").append(this.getMsgFormat()).append(' ');
		builder.append("srcTermId:").append(this.getSrcTermId()).append(' ');
		builder.append("reciveTime:").append(this.recvTime).append(' ');
		builder.append("destTermId:").append(this.getDestTermId()).append(' ');
		builder.append("MSGLENGTH:").append(this.getMsgLength()).append(' ');
		builder.append("MSGCONTENT:").append(this.getMsgContent()).append(' ');
		builder.append("STATE:").append(this.getState()).append(' ');
		builder.append('\n');
		return builder.toString();
	}


	public void setSmmsgContent(String smmsgContent) {
		this.smmsgContent = smmsgContent;
	}

	public String getSmmsgContent() {
		return smmsgContent;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public boolean isReport() {
		return isReport;
	}

	public void setReport(boolean isReport) {
		this.isReport = isReport;
	}
}
