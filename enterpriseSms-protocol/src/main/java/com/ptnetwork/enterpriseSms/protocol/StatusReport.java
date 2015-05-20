/**
 * 
 */
package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * 状态报告
 * 状态报告的内容格式为:8(Msg_Id) + 7(Stat) + 10(Submit_time) + 10(Done_time) + 21(Dest_terminal_Id) + 4(SMSC_sequence)
 * @author jasonzhang
 *
 */
public class StatusReport {

	private long msgId;
	private String stat;
	private String submitTime;
	private String doneTime;
	private String destTerminalId;
	private int smscSequence;
	public long getMsgId() {
		return msgId;
	}
	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public String getDoneTime() {
		return doneTime;
	}
	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}
	public String getDestTerminalId() {
		return destTerminalId;
	}
	public void setDestTerminalId(String destTerminalId) {
		this.destTerminalId = destTerminalId;
	}
	public int getSmscSequence() {
		return smscSequence;
	}
	public void setSmscSequence(int smscSequence) {
		this.smscSequence = smscSequence;
	}
	
	public void decode(InputStream in) throws IOException {
		this.msgId = Utils.bytes2long(in);
		this.stat = Utils.bytes2string(in, 7);
		this.submitTime = Utils.bytes2string(in, 10);
		this.doneTime = Utils.bytes2string(in, 10);
		this.destTerminalId = Utils.bytes2string(in, 21);
		this.smscSequence = Utils.bytes2int(in);
	}
	
	public void decode(byte[] data, int start) throws IOException {
		this.msgId = Utils.bytes2long(data, start);
		this.stat = Utils.bytes2string(data, start + 8, 7);
		this.submitTime = Utils.bytes2string(data, start + 8 + 7, 10);
		this.doneTime = Utils.bytes2string(data, start + 8 + 7 + 10, 10);
		this.destTerminalId = Utils.bytes2string(data,start + 8 + 7 + 10 + 10, 21);
		this.smscSequence = Utils.bytes2int(data, start + 8 + 7 + 10 + 10 + 21);
	}
}
