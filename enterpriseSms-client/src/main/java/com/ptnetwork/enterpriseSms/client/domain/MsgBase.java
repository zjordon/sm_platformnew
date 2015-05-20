/**
 * @(#) MsgBase.java 2006-3-2
 * 
 * Copyright 2006 ptnetwork
 */
package com.ptnetwork.enterpriseSms.client.domain;

/**
 * @author jasonzhang
 *
 */
public abstract class MsgBase extends Header {

	private int msgFormat = 15;//����Ϣ��ʽ,Ĭ��Ϊ15
	private String srcTermId;//����Ϣ�����û�����.
	protected String destTermId;//����Ϣ���պ���.
	private int msgLength;//����Ϣ����,��smgp�з�װʱ���Զ�����
	protected String msgContent;//����Ϣ����
	private String reserve;//����
	
	private int state;

	public String getDestTermId() {
		return destTermId;
	}

	public void setDestTermId(String destTermId) {
		this.destTermId = destTermId;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public int getMsgFormat() {
		return msgFormat;
	}

	public void setMsgFormat(int msgFormat) {
		this.msgFormat = msgFormat;
	}

	public int getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public String getSrcTermId() {
		return srcTermId;
	}

	public void setSrcTermId(String srcTermId) {
		this.srcTermId = srcTermId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
