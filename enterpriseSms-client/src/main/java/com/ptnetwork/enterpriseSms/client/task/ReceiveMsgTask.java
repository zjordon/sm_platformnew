/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.queue.QueueManager;
import com.ptnetwork.enterpriseSms.client.task.handler.HandlerException;
import com.ptnetwork.enterpriseSms.client.task.handler.ReceiveMsgHandler;
import com.ptnetwork.enterpriseSms.protocol.DeliverPacket;

/**
 * 处理下发信息任务
 * @author jasonzhang
 *
 */
public class ReceiveMsgTask extends AbstractTask {
	private static final Logger logger = Logger.getLogger(ReceiveMsgTask.class);

	/* (non-Javadoc)
	 * @see com.ptnetwork.enterpriseSms.client.task.AbstractTask#executeTask()
	 */
	@Override
	protected int executeTask() {
		List<DeliverPacket> receiveMsgList = QueueManager.getInstance().getReceiveMsgList(100);
		if (!receiveMsgList.isEmpty()) {
			//TODO 保存成文件
			for (DeliverPacket data : receiveMsgList) {
				try {
					ReceiveMsgHandler.getInstance().handle(data);
				} catch (HandlerException e) {
					logger.error("exception happen when executeTask", e);
				}
			}
			//TODO 把文件标识成已经理
		}
		logger.info("ReceiveMsgTask size is " + receiveMsgList.size());
		return receiveMsgList.size();
	}

}
