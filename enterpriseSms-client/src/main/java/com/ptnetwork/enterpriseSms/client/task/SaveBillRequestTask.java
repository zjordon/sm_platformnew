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

/**
 * @author jasonzhang
 *
 */
public class SaveBillRequestTask extends AbstractTask {
	private static final Logger logger = Logger.getLogger(SaveBillRequestTask.class);

	/* (non-Javadoc)
	 * @see com.ptnetwork.enterpriseSms.client.task.AbstractTask#executeTask()
	 */
	@Override
	protected int executeTask() {
		List<BillRequest> newBillRequestList = QueueManager.getInstance().getNewBillRequestList(100);
		if (!newBillRequestList.isEmpty()) {
			try {
				DaoManager.getInstance().getBillRequestStore().saveBillRequestList(newBillRequestList);
			} catch (DaoException e) {
				logger.error("exception happen when executeTask", e);
			}
		}
		List<BillRequest> oldBillRequestList = QueueManager.getInstance().getOldBillRequestList(100);
		if (!oldBillRequestList.isEmpty()) {
			try {
				DaoManager.getInstance().getBillRequestStore().updateBillRequestList(oldBillRequestList);
			} catch (DaoException e) {
				logger.error("exception happen when executeTask", e);
			}
		}
		logger.info("SaveBillRequestTask size is " + (newBillRequestList.size() + oldBillRequestList.size()));
		return newBillRequestList.size() + oldBillRequestList.size();
	}

}
