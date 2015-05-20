/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.domain;

import java.util.Date;

/**
 * @author jasonzhang
 *
 */
public class RepestRequestRecord {

	private String id;
	private Date startTime;
	private Date endTime;
	private String responseState;
	private Date createDate;
	private String billRequestId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getBillRequestId() {
		return billRequestId;
	}
	public void setBillRequestId(String billRequestId) {
		this.billRequestId = billRequestId;
	}
	
	
}
