/**
 * Project Name:enterpriseSms-client
 * File Name:AbstractSequence.java
 * Package Name:com.ptnetwork.enterpriseSms.client.common
 * Date:2013-2-9下午4:53:38
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.common;
/**
 * ClassName:AbstractSequence <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-2-9 下午4:53:38 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public abstract class AbstractSequence implements Sequence {

	protected int minId = 0;
	protected int maxId = 0xFFFFFFFF;
	protected int currentId;
	protected String sequenceName;

	@Override
	public int currentId() {
		
		return this.currentId;
	}

	@Override
	public abstract int netxtId();

	@Override
	public void setMinId(int minId) {
		
		this.minId = minId;
	}

	@Override
	public void setMaxId(int maxId) {
		
		this.maxId = maxId;
	}

	@Override
	public void setSequenceName(String sequenceName) {
		
		this.sequenceName = sequenceName;
		
	}
	
	public String getSequenceName() {
		return this.sequenceName;
	}
}

