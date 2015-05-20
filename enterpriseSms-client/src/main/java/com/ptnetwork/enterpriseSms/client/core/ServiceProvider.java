/**
 * Project Name:smPlatform
 * File Name:ServiceProvider.java
 * Package Name:com.ptnetwork.enterpriseSms.client.core
 * Date:2012-11-27����7:50:09
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
*/
/**
 * Project Name:smPlatform
 * File Name:ServiceProvider.java
 * Package Name:com.ptnetwork.enterpriseSms.client.core
 * Date:2012-11-27����7:50:09
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.client.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.domain.Login;
import com.ptnetwork.enterpriseSms.client.exception.LoginException;
import com.ptnetwork.enterpriseSms.client.net.ConnectionInterface;
import com.ptnetwork.enterpriseSms.client.result.LoginResult;

/**
 * ClassName:ServiceProvider <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2012-11-27 ����7:50:09 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public abstract class ServiceProvider {

	private static Log log = LogFactory.getLog(ServiceProvider.class);
	
	
	private boolean reLogining = false;
	
	protected ConnectionInterface smsClient;
	private Login login;
	
	public void setLogin(Login login) {
		this.login = login;
	}
	public void setSmsClient(ConnectionInterface smsClient) {
		this.smsClient = smsClient;
	}
	public ConnectionInterface getSmsClient() {
		return smsClient;
	}
	
	public void login() {
//		smsClient = new SmsClient();
		LoginResult loginResult = null;
		Exception exception = null;
		log.info("start login");
		try { 
			loginResult = doLogin();
		} catch (LoginException e) {
			log.error(e.getMessage(), e);
			exception = e;
		}
		if (exception != null) {
			reLogin();
			return;
		}
		if (loginResult == null || loginResult.getResultCode() != LoginResult.LOGIN_SUCCESS) {
			log.error("login error!");
			if (loginResult != null) {
				log.error("loginResult code is " + loginResult.getResultCode());
			}
			System.exit(1);
		} else {
			log.info("login success with stat " + loginResult.getResultCode());
		}

		afterLogin();
		this.reLogining = false;
	}
	
	protected void reLogin() {
		synchronized(this) {
			if (!this.reLogining) {
				this.reLogining = true;
			} else {
				log.info("already relogin!");
				return;
			}
		}
		log.info("re login!!!!");
		beforeReLogin();
		try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			
		}
		this.reLogining = false;
		login();
	}
	
	protected abstract void beforeReLogin();
	
	protected LoginResult doLogin() throws LoginException {
		LoginResult loginResult = null;
		loginResult = this.smsClient.login(login);
		return loginResult;
	}
	
	protected abstract void afterLogin();
	
	public void exit() {
		this.smsClient.logout();
		afterExit();
	}
	
	protected abstract void afterExit();
	
	protected void activeTest() {
		//TODO:����Щ���⣿
//		client.startActiveTest(testInterTime, activeTestTimes, idleTime);
	}

}

