/**
 * Project Name:smPlatform
 * File Name:LoginResult.java
 * Package Name:com.ptnetwork.enterpriseSms.client.core
 * Date:2012-11-27����7:56:14
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
*/
/**
 * Project Name:smPlatform
 * File Name:LoginResult.java
 * Package Name:com.ptnetwork.enterpriseSms.client.core
 * Date:2012-11-27����7:56:14
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.client.result;
/**
 * ���Ͷ��ŵĽ��
 * ClassName:LoginResult <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2012-11-27 ����7:56:14 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class SubmitResult {
	public final static int SEND_SUCCESS = 1;
	public final static int SEND_FAIL = 0;

	//����״̬��
	private int resultCode;
	//������Ϣ
	private String resultMessage;
	private int smId;
	public int getResultCode() {
		return resultCode;
		
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public void setSmId(int smId) {
		this.smId = smId;
	}
	public int getSmId() {
		return smId;
	}
	
	
}

