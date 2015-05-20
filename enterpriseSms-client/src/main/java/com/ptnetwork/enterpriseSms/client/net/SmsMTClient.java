/**
 * Project Name:enterpriseSms-client
 * File Name:SmsMTClient.java
 * Package Name:com.ptnetwork.enterpriseSms.client.net
 * Date:2013-1-27下午2:17:41
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.core.DeliverListener;
import com.ptnetwork.enterpriseSms.client.domain.Login;
import com.ptnetwork.enterpriseSms.client.exception.LoginException;
import com.ptnetwork.enterpriseSms.client.result.LoginResult;

/**
 * ClassName:SmsMTClient <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-27 下午2:17:41 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class SmsMTClient implements ConnectionInterface, DeliverInterface {
	private static Log log = LogFactory.getLog(SmsMTClient.class);

	@Override
	public void setDeliverListener(DeliverListener listener) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public LoginResult login(Login login) throws LoginException {
		
		LoginResult loginResult = new LoginResult();
		loginResult.setResultCode(LoginResult.LOGIN_SUCCESS);
		log.info("mt login");
		// TODO Auto-generated method stub
		return loginResult;
	}

	@Override
	public void logout() {
		
		// TODO Auto-generated method stub
		log.info("mt logout");
		
	}

	@Override
	public void enableActiveTest() {
		
		// TODO Auto-generated method stub
		
	}

}

