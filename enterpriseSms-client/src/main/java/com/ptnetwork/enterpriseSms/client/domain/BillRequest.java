/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.domain;

import java.util.Date;

/**
 * 计费请求
 * @author jasonzhang
 *
 */
public class BillRequest {

	private String id;
	private String userName;
	private String userPass;
	private String instruct;
	private long msisdn;
	private boolean repeatFlag;
	private int state;
	private Date startTime;
	private Date endTime;
	private String responseState;
	private Date createDate;
	private String deliverId;
	private String postUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getInstruct() {
		return instruct;
	}
	public void setInstruct(String instruct) {
		this.instruct = instruct;
	}
	public long getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(long msisdn) {
		this.msisdn = msisdn;
	}
	public boolean isRepeatFlag() {
		return repeatFlag;
	}
	public void setRepeatFlag(boolean repeatFlag) {
		this.repeatFlag = repeatFlag;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getResponseState() {
		return responseState;
	}
	public void setResponseState(String responseState) {
		this.responseState = responseState;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDeliverId() {
		return deliverId;
	}
	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}
	
	public String getPostUrl() {
		return postUrl;
	}
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id:").append(this.id).append("userName:").append(this.userName).append("userPass:").append(this.userPass);
		builder.append("instruct:").append(this.instruct).append("msisdn:").append(this.msisdn).append("state:").append(this.state);
		builder.append("startTime:").append(this.startTime).append("endTime:").append(this.endTime).append("responseState:").append(this.responseState);
		builder.append("createDate:").append(this.createDate).append("deliverId:").append(this.deliverId).append("postUrl:").append(this.postUrl).append('\n');
		return builder.toString();
	}
	
	
}
