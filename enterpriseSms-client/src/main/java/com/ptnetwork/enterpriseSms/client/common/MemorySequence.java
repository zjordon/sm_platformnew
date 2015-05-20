/**
 * Project Name:enterpriseSms-client
 * File Name:MemorySequence.java
 * Package Name:com.ptnetwork.enterpriseSms.client.common
 * Date:2013-2-9下午4:41:27
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.common;
/**
 * ClassName:MemorySequence <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-2-9 下午4:41:27 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class MemorySequence extends AbstractSequence {
	

	@Override
	public int netxtId() {
		
		if (super.currentId == maxId) {
			super.currentId = super.minId;
		}
		super.currentId++;
		return super.currentId;
	}
}

