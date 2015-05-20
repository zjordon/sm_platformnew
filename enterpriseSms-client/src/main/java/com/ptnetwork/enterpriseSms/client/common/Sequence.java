/**
 * Project Name:enterpriseSms-client
 * File Name:Sequence.java
 * Package Name:com.ptnetwork.enterpriseSms.client.common
 * Date:2013-2-9下午4:35:50
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.common;
/**
 * ClassName:Sequence <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-2-9 下午4:35:50 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public interface Sequence {

	int currentId();
	int netxtId();
	void setMinId(int minId);
	void setMaxId(int maxId);
	void setSequenceName(String sequenceName);
}

