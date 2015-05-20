package com.ptnetwork.enterpriseSms.client.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ptnetwork.enterpriseSms.client.common.UUIDGenerator;
import com.ptnetwork.enterpriseSms.client.domain.BillRequest;

public class DbBillRequestStoreTest extends BaseStoreTest {

	private DbBillRequestStore dbBillRequestStore;
	protected void setUp() throws Exception {
		super.setUp();
		this.dbBillRequestStore = new DbBillRequestStore();
		this.dbBillRequestStore.setDataSource(super.dataSource);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

//	public void testSaveBillRequetList() throws DaoException {
//		List<BillRequest> list = new ArrayList<BillRequest>();
//		Date currentDate = new Date();
//		for (int i=0; i<10; i++) {
//			BillRequest billRequest = new BillRequest();
//			billRequest.setId((new UUIDGenerator()).generate());
//			billRequest.setUserName("test");
//			billRequest.setUserPass("test");
//			billRequest.setInstruct(Integer.toString(i));
//			billRequest.setMsisdn(13950079348L);
//			if (i%2 == 0) {
//				billRequest.setRepeatFlag(false);
//			} else {
//				billRequest.setRepeatFlag(true);
//			}
//			billRequest.setState(0);
//			billRequest.setStartTime(currentDate);
//			billRequest.setEndTime(currentDate);
//			billRequest.setResponseState("1");
//			billRequest.setCreateDate(currentDate);
//			billRequest.setDeliverId(Integer.toString(i));
//			list.add(billRequest);
//		}
//		this.dbBillRequestStore.saveBillRequetList(list);
//	}

//	public void testUpdateBillRequestList() throws DaoException {
//		List<BillRequest> list = new ArrayList<BillRequest>();
//		List<BillRequest> repeatList = new ArrayList<BillRequest>();
//		Date currentDate = new Date();
//		for (int i=0; i<10; i++) {
//			BillRequest billRequest = new BillRequest();
//			billRequest.setId((new UUIDGenerator()).generate());
//			billRequest.setUserName("test");
//			billRequest.setUserPass("test");
//			billRequest.setInstruct(Integer.toString(i));
//			billRequest.setMsisdn(13950079348L);
//			if (i%2 == 0) {
//				billRequest.setRepeatFlag(false);
//			} else {
//				billRequest.setRepeatFlag(true);
//			}
//			billRequest.setState(0);
//			billRequest.setStartTime(currentDate);
//			billRequest.setEndTime(currentDate);
//			billRequest.setResponseState("1");
//			billRequest.setCreateDate(currentDate);
//			billRequest.setDeliverId(Integer.toString(i));
//			list.add(billRequest);
//			if (billRequest.isRepeatFlag()) {
//				repeatList.add(billRequest);
//			}
//		}
//		this.dbBillRequestStore.saveBillRequetList(list);
//		for (int i=0; i<repeatList.size(); i++) {
//			if (i%2 == 0) {
//				repeatList.get(i).setRepeatFlag(false);
//			}
//		}
//		this.dbBillRequestStore.updateBillRequestList(repeatList);
//	}

	public void testGetNeedRepeatRequestList() throws DaoException {
		List<BillRequest> list = this.dbBillRequestStore.getNeedRepeatRequestList(100);
		if (!list.isEmpty()) {
			System.out.println(list.size());
		}
	}

}
