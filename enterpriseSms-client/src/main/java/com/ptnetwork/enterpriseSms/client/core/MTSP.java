/**
 * Project Name:enterpriseSms-client
 * File Name:MTSP.java
 * Package Name:com.ptnetwork.enterpriseSms.client.core
 * Date:2013-1-27下午1:56:14
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.core;

import com.ptnetwork.enterpriseSms.client.domain.Deliver;
import com.ptnetwork.enterpriseSms.client.net.DeliverInterface;
import com.ptnetwork.enterpriseSms.client.persistence.DbDeliverStore;
import com.ptnetwork.enterpriseSms.protocol.DeliverPacket;

/**
 * ClassName:MTSP <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-27 下午1:56:14 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class MTSP extends ServiceProvider implements DeliverListener {
	private DbDeliverStore deliverStore;
	

	public void setDeliverStore(DbDeliverStore deliverStore) {
		this.deliverStore = deliverStore;
	}

	@Override
	protected void beforeReLogin() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterLogin() {
		
		((DeliverInterface)super.smsClient).setDeliverListener(this);
		
	}

	@Override
	protected void afterExit() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDeliver(DeliverPacket deliverPacket) {
		// TODO Auto-generated method stub
		
	}

}

