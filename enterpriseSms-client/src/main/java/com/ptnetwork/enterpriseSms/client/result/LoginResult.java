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
 * ��¼��ػ�mas��Ľ��
 * ClassName:LoginResult <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2012-11-27 ����7:56:14 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class LoginResult {
	public final static int LOGIN_SUCCESS = 1;

	//����״̬��
	private int resultCode;
	//������Ϣ
	private String resultMessage;
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
	
	
}

