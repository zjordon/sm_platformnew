package com.ptnetwork.enterpriseSms.client.persistence;

import java.util.List;

import com.ptnetwork.enterpriseSms.client.domain.Channel;

public class DbChannelStoreTest extends BaseStoreTest {

	private DbChannelStore dbChannelStore;
	protected void setUp() throws Exception {
		super.setUp();
		this.dbChannelStore = new DbChannelStore();
		this.dbChannelStore.setDataSource(super.dataSource);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSaveChannel() throws DaoException {
		Channel channel = new Channel();
		channel.setId("1");
		channel.setPostUrl("http://www.baidu.com");
		this.dbChannelStore.saveChannel(channel);
	}

	public void testUpdatePostUrl() throws DaoException {
		this.dbChannelStore.updatePostUrl("1", "http://www.google.com");
	}

	public void testGetChannelList() throws DaoException {
		List<Channel> list = this.dbChannelStore.getChannelList();
		super.assertNotNull(list);
	}

}
