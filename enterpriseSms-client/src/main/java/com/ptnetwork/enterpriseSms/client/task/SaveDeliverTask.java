/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.domain.Deliver;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;
import com.ptnetwork.enterpriseSms.client.queue.QueueManager;

/**
 * @author jasonzhang
 *
 */
public class SaveDeliverTask extends AbstractTask {
	private static final Logger logger = Logger.getLogger(SaveDeliverTask.class);

	/* (non-Javadoc)
	 * @see com.ptnetwork.enterpriseSms.client.task.AbstractTask#executeTask()
	 */
	@Override
	protected int executeTask() {
		List<Deliver> deliverList = QueueManager.getInstance().getDeliverList(100);
		//不保存到数据库存，只保存到日志
		if (!deliverList.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (Deliver deliver : deliverList) {
				builder.append(deliver.toString());
			}
			logger.info(builder.toString());
//			try {
//				DaoManager.getInstance().getDeliverStore().saveDeliverList(deliverList);
//			} catch (DaoException e) {
//				logger.error("exception happen when executeTask", e);
//			}
		}
		logger.info("SaveDeliverTask size is " + deliverList.size());
		return deliverList.size();
	}

}
