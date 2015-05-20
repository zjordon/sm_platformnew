/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.queue;

import java.util.List;

import com.ptnetwork.enterpriseSms.client.core.DeliverListener;
import com.ptnetwork.enterpriseSms.client.domain.BillRequest;
import com.ptnetwork.enterpriseSms.client.domain.Deliver;
import com.ptnetwork.enterpriseSms.protocol.AbstractPacket;
import com.ptnetwork.enterpriseSms.protocol.DeliverPacket;

/**
 * @author jasonzhang
 *
 */
public class QueueManager implements DeliverListener {
	
	private final static QueueManager instance = new QueueManager();
	public final static QueueManager getInstance() {
		return instance;
	}
	
	private QueueManager(){}

	private GeneralQueue<Deliver> deliverQueue = new GeneralQueue<Deliver>();
	private GeneralQueue<BillRequest> newBillRequestQueue = new GeneralQueue<BillRequest>();
	private GeneralQueue<BillRequest> oldBillRequestQueue = new GeneralQueue<BillRequest>();
	private GeneralQueue<DeliverPacket> receiveMsgQueue = new GeneralQueue<DeliverPacket>();
	private GeneralQueue<AbstractPacket> sendMsgQueue = new GeneralQueue<AbstractPacket>();
	
	public void addDeliver(Deliver deliver) {
		this.deliverQueue.add(deliver);
	}
	
	public List<Deliver> getDeliverList(int num) {
		return this.deliverQueue.popTopElement(num);
	}
	
	public void addNewBillRequest(BillRequest billRequest) {
		this.newBillRequestQueue.add(billRequest);
	}
	
	public List<BillRequest> getNewBillRequestList(int num) {
		return this.newBillRequestQueue.popTopElement(num);
	}
	
	public void addOldBillRequest(BillRequest billRequest) {
		this.oldBillRequestQueue.add(billRequest);
	}
	
	public List<BillRequest> getOldBillRequestList(int num) {
		return this.oldBillRequestQueue.popTopElement(num);
	}
	
	public List<DeliverPacket> getReceiveMsgList(int num) {
		return this.receiveMsgQueue.popTopElement(num);
	}
	
	public void addSendMsg(AbstractPacket packet) {
		this.sendMsgQueue.add(packet);
	}
	
	public List<AbstractPacket> getSendMsgList(int num) {
		return this.sendMsgQueue.popTopElement(num);
	}

	@Override
	public void addDeliver(DeliverPacket deliverPacket) {
		this.receiveMsgQueue.add(deliverPacket);
		
	}
}
