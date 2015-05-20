package com.ptnetwork.enterpriseSms.client.task.handler;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;
import com.ptnetwork.enterpriseSms.protocol.DeliverPacket;
import com.ptnetwork.enterpriseSms.protocol.PacketHead;

import junit.framework.TestCase;

public class ReceiveMsgHandlerTest extends TestCase {

	private ReceiveMsgHandler handler;
	protected void setUp() throws Exception {
		super.setUp();
		DaoManager.getInstance().init();
		CacheManager.getInstance().init();
		this.handler = ReceiveMsgHandler.getInstance();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHandle() throws HandlerException {
		PacketHead packetHead = new PacketHead();
		
		DeliverPacket data = new DeliverPacket(packetHead);
		data.setDestTermId("106575322484");
		data.setIsReport((byte)0);
		data.setMsgContent("10000000100000000001");
		data.setMsgFormat((byte)8);
		data.setMsgId(123L);
		data.setMsgLength((byte)data.getMsgContent().length());
		data.setServiceId("123");
		data.setSrcTermId("15750750286");
		this.handler.handle(data);
	}

}
