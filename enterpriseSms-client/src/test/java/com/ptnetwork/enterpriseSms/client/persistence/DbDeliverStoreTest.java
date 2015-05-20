package com.ptnetwork.enterpriseSms.client.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ptnetwork.enterpriseSms.client.domain.Deliver;
import com.ptnetwork.enterpriseSms.client.common.UUIDGenerator;

public class DbDeliverStoreTest extends BaseStoreTest {

	private DbDeliverStore dbDeliverStore;
	protected void setUp() throws Exception {
		super.setUp();
		this.dbDeliverStore = new DbDeliverStore();
		this.dbDeliverStore.setDataSource(super.dataSource);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSaveDeliverList() throws DaoException {
		List<Deliver> deliverList = new ArrayList<Deliver>();
		Date currentDate = new Date();
		for (int i=0; i<10; i++) {
			Deliver deliver = new Deliver();
			deliver.setId((new UUIDGenerator()).generate());
			deliver.setMsgId(i);
			deliver.setCreateDate(currentDate);
			deliver.setDestTermId("106575322484");
			deliver.setMsgContent(Integer.toString(i));
			deliver.setMsgFormat(8);
			deliver.setMsgLength(deliver.getMsgContent().length());
			deliver.setReport(false);
			deliver.setServiceId("123");
			deliver.setSrcTermId("13950079348");
			deliver.setState(0);
			deliverList.add(deliver);
		}
		this.dbDeliverStore.saveDeliverList(deliverList);
	}

}