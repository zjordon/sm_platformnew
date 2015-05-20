/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.scheduler;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.task.Task;

/**
 * @author jasonzhang
 *
 */
public class SingleTaskThread extends AbstractTaskThread {
	private static final Logger logger = Logger.getLogger(SingleTaskThread.class);
	private Task task;

	public SingleTaskThread(Task task) {
		super();
		this.task = task;
	}

	@Override
	public void run() {
		super.stop = false;
		logger.info("start run task");
		while (!super.stop) {
			if (!task.isStop()) {
				logger.info("start execute task");
				int nums = this.task.execute();
				if (nums == 0) {
					logger.info("execute nums is 0 sleep 10 seconds");
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					logger.info("execute nums is " + nums);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				super.stop = true;
			}
			
		}
		logger.info("end run task");
		
	}
	
	
}
