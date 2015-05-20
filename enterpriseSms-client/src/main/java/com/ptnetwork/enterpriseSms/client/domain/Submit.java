/**
 * @(#) Submit.java 2006-3-2
 * 
 * Copyright 2006 ptnetwork
 */
package com.ptnetwork.enterpriseSms.client.domain;

import com.ptnetwork.enterpriseSms.protocol.SubmitPacket;

/**
 * @author jasonzhang
 *
 */
public class Submit extends Header {
	private SubmitPacket submitPacket;
	
	public final static int STATE_WAITING = 0;
	public final static int STATE_SENDIGN = 1;
	
	private int instructionId;//对应的指令主键
	private int trafficId;//对应的业务主键
	private String msgId;
	private int state;
	
	private long sendTime;//发送短信的时间

	public int getTrafficId() {
		return trafficId;
	}

	public void setTrafficId(int trafficId) {
		this.trafficId = trafficId;
	}

	public int getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(int instructionId) {
		this.instructionId = instructionId;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Submit() {
		
		setState(STATE_WAITING);
		submitPacket = new SubmitPacket();
		setSrcTermId("10658463719");
	}
	
	public String getAtTime() {
		return this.submitPacket.getAtTime();
	}
	public void setAtTime(String atTime) {
		this.submitPacket.setAtTime(atTime);
	}
	public String getChargeTermId() {
		return this.submitPacket.getChargeTermId();
	}
	public void setChargeTermId(String chargeTermId) {
		this.submitPacket.setChargeTermId(chargeTermId);
	}
	public int getDestTermIdCount() {
		return this.submitPacket.getDestTermIdCount();
	}
	public void setDestTermIdCount(int destTermIdCount) {
		this.submitPacket.setDestTermIdCount((byte)destTermIdCount);
	}
	public String getFeeCode() {
		return this.submitPacket.getFeeCode();
	}
	public void setFeeCode(String feeCode) {
		this.submitPacket.setFeeCode(feeCode);
	}
	public String getFeeType() {
		return this.submitPacket.getFeeType();
	}
	public void setFeeType(String feeType) {
		this.submitPacket.setFeeType(feeType);
	}
	public String getFixedFee() {
		return this.submitPacket.getFixedFee();
	}
	public void setFixedFee(String fixedFee) {
		this.submitPacket.setFixedFee(fixedFee);
	}
	public int getMsgType() {
		return this.submitPacket.getMsgType();
	}
	public void setMsgType(int msgType) {
		this.submitPacket.setMsgType((byte)msgType);
	}
	public int getNeedReport() {
		return this.submitPacket.getNeedReport();
	}
	public void setNeedReport(int needReport) {
		this.submitPacket.setNeedReport((byte)needReport);
	}
	public int getPriority() {
		return this.submitPacket.getPriority();
	}
	public void setPriority(int priority) {
		this.submitPacket.setPriority((byte)priority);
	}
	public String getServiceId() {
		return this.submitPacket.getServiceId();
	}
	public void setServiceId(String serviceId) {
		this.submitPacket.setServiceId(serviceId);
	}
	public String getValidTime() {
		return this.submitPacket.getValidTime();
	}
	public void setValidTime(String validTime) {
		this.submitPacket.setValidTime(validTime);
	}
	
	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getDestTermId() {
		return this.submitPacket.getDestTermId();
	}

	public void setDestTermId(String destTermId) {
		this.submitPacket.setDestTermId(destTermId);
	}

	public String getMsgContent() {
		return this.submitPacket.getMsgContent();
	}

	public void setMsgContent(String msgContent) {
		this.submitPacket.setMsgContent(msgContent);
	}

	public int getMsgFormat() {
		return this.submitPacket.getMsgFormat();
	}

	public void setMsgFormat(int msgFormat) {
		this.submitPacket.setMsgFormat((byte)msgFormat);
	}

	public int getMsgLength() {
		return this.submitPacket.getMsgLength();
	}

	public void setMsgLength(int msgLength) {
		this.submitPacket.setMsgLength((byte)msgLength);
	}

	public String getReserve() {
		return this.submitPacket.getReserve();
	}

	public void setReserve(String reserve) {
		this.submitPacket.setReserve(reserve);
	}

	public String getSrcTermId() {
		return this.submitPacket.getSrcTermId();
	}

	public void setSrcTermId(String srcTermId) {
		this.submitPacket.setSrcTermId(srcTermId);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public SubmitPacket getSubmitPacket() {
		return submitPacket;
	}
	
	public void setSequenceId(int sequenceId) {
		this.submitPacket.setSequenceId(sequenceId);
	}
	
	public int getSequenceId() {
		return this.submitPacket.getSequenceId();
	}
	
}
