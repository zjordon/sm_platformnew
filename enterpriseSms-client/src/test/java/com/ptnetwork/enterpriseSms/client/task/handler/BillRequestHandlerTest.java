package com.ptnetwork.enterpriseSms.client.task.handler;

import java.util.Date;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.common.UUIDGenerator;
import com.ptnetwork.enterpriseSms.client.domain.BillRequest;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;

import junit.framework.TestCase;

public class BillRequestHandlerTest extends TestCase {

	private BillRequestHandler handler;
	protected void setUp() throws Exception {
		super.setUp();
		DaoManager.getInstance().init();
		CacheManager.getInstance().init();
		this.handler = BillRequestHandler.getInstance();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHandle() throws HandlerException {
		Date currentDate = new Date();
		BillRequest billRequest = new BillRequest();
		billRequest.setId((new UUIDGenerator()).generate());
		billRequest.setUserName("test");
		billRequest.setUserPass("cc03e747a6afbbcbf8be7668acfebee5");
		billRequest.setInstruct("10000000100000000001");
		billRequest.setMsisdn(13950079348L);
		billRequest.setState(0);
		billRequest.setStartTime(currentDate);
		billRequest.setEndTime(currentDate);
		billRequest.setCreateDate(currentDate);
		billRequest.setDeliverId("1");
		this.handler.handle(billRequest);
		super.assertNotNull(billRequest.getResponseState());
		System.out.println(billRequest.getResponseState());
		super.assertEquals(1, billRequest.getState());
	}

}
