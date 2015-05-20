/**
 * Project Name:enterpriseSms-client
 * File Name:ConnectionInterface.java
 * Package Name:com.ptnetwork.enterpriseSms.client.net
 * Date:2013-1-27上午11:34:03
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.net;

import com.ptnetwork.enterpriseSms.client.domain.Login;
import com.ptnetwork.enterpriseSms.client.exception.LoginException;
import com.ptnetwork.enterpriseSms.client.result.LoginResult;

/**
 * ClassName:ConnectionInterface <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-27 上午11:34:03 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public interface ConnectionInterface {

	LoginResult login(Login login) throws LoginException;
	
	void logout();
	
	void enableActiveTest();
}

