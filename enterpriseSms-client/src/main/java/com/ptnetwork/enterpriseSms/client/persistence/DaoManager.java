/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.persistence;

import javax.sql.DataSource;

import com.ptnetwork.enterpriseSms.client.common.DataSourceUtil;

/**
 * @author jasonzhang
 *
 */
public class DaoManager {

	private final static DaoManager instance = new DaoManager();
	
	public final static DaoManager getInstance() {
		return instance;
	}
	
	private DaoManager(){}
	
	private DbDeliverStore deliverStore;
	private DbBillRequestStore billRequestStore;
	private DbChannelUserStore channelUserStore;
	private DbEventStore eventStore;
	private DbChannelInstructStore channelInstructStore;
	
	public void init() {
		DataSource dataSource = DataSourceUtil.getDataSource();
		if (dataSource != null) {
			this.deliverStore = new DbDeliverStore();
			this.deliverStore.setDataSource(dataSource);
			this.billRequestStore = new DbBillRequestStore();
			this.billRequestStore.setDataSource(dataSource);
			this.channelUserStore = new DbChannelUserStore();
			this.channelUserStore.setDataSource(dataSource);
			this.eventStore = new DbEventStore();
			this.eventStore.setDataSource(dataSource);
			this.channelInstructStore = new DbChannelInstructStore();
			this.channelInstructStore.setDataSource(dataSource);
		}
	}

	public DbDeliverStore getDeliverStore() {
		return deliverStore;
	}

	public DbBillRequestStore getBillRequestStore() {
		return billRequestStore;
	}

	public DbChannelUserStore getChannelUserStore() {
		return channelUserStore;
	}

	public DbEventStore getEventStore() {
		return eventStore;
	}

	public DbChannelInstructStore getChannelInstructStore() {
		return channelInstructStore;
	}
	
}
