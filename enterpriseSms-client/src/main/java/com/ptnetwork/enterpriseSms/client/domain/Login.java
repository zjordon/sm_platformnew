/**
 * Project Name:smPlatform
 * File Name:Login.java
 * Package Name:com.ptnetwork.enterpriseSms.client.domain
 * Date:2012-11-28����9:47:20
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.domain;

/**
 * ClassName:Login <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2012-11-28 ����9:47:20 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class Login {

	//������ip
	private String ip;
	//������˿�
	private int port;
	//��¼�û���
	private String userName;
	//��¼����
	private String password;
	
	private int loginMode;//登录类型（0＝发送短消息，1＝接收短消息,2＝收发短消息
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getLoginMode() {
		return loginMode;
	}
	public void setLoginMode(int loginMode) {
		this.loginMode = loginMode;
	}
}

