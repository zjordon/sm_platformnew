package com.ptnetwork.enterpriseSms.client.persistence;

import java.util.List;

import com.ptnetwork.enterpriseSms.client.domain.ChannelUser;

public class DbChannelUserStoreTest extends BaseStoreTest {

	private DbChannelUserStore dbChannelUserStore;
	protected void setUp() throws Exception {
		super.setUp();
		this.dbChannelUserStore = new DbChannelUserStore();
		this.dbChannelUserStore.setDataSource(super.dataSource);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetChannelUserList() throws DaoException {
		List<ChannelUser> list = this.dbChannelUserStore.getChannelUserList();
		if (!list.isEmpty()) {
			System.out.println("size is " + list.size());
		} else {
			System.out.println("list is empty");
		}
	}

}
