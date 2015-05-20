/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.domain.BillRequest;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;
import com.ptnetwork.enterpriseSms.client.queue.QueueManager;
import com.ptnetwork.enterpriseSms.client.task.handler.BillRequestHandler;
import com.ptnetwork.enterpriseSms.client.task.handler.HandlerException;

/**
 * @author jasonzhang
 *
 */
public class ReBillRequestTask extends AbstractTask {
	private static final Logger logger = Logger.getLogger(ReBillRequestTask.class);

	/* (non-Javadoc)
	 * @see com.ptnetwork.enterpriseSms.client.task.AbstractTask#executeTask()
	 */
	@Override
	protected int executeTask() {
		List<BillRequest> billRequestList = null;
		try {
			billRequestList = DaoManager.getInstance().getBillRequestStore().getNeedRepeatRequestList(100);
		} catch (DaoException e) {
			logger.error("exception happen when executeTask", e);
		}
		if (billRequestList != null && !billRequestList.isEmpty()) {
			for (BillRequest billRequest : billRequestList) {
				HandlerException he = null;
				try {
					BillRequestHandler.getInstance().handle(billRequest);
				} catch (HandlerException e) {
					he = e;
					logger.error("exception happen when executeTask", e);
				}
				if (he == null) {
					QueueManager.getInstance().addOldBillRequest(billRequest);
				} else {
					he = null;
				}
			}
			logger.info("ReBillRequestTask size is " + billRequestList.size());
			return billRequestList.size();
		}
		return 0;
	}

}
