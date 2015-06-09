/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.task.Task;

/**
 * @author jasonzhang
 *
 */
public class MultileTaskThread extends AbstractTaskThread {
	private static final Logger logger = Logger.getLogger(MultileTaskThread.class);
	
	private List<Task> tasks = new ArrayList<Task>();
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}

	/* (non-Javadoc)
	 * @see com.jason.ddoMsg.scheduler.AbstractTaskThread#run()
	 */
	@Override
	public void run() {
		super.stop = false;
		logger.info("start run task");
		while (!super.stop) {
			logger.info("start execute task");
			if (!this.tasks.isEmpty()) {
				int stopNums = 0;
				int allExecuteNums = 0;
				for (Task task : this.tasks) {
					if (!task.isStop()) {
						int nums = task.execute();
						if (nums == 0) {
							logger.info("execute nums is 0");
						} else {
							allExecuteNums += nums;
							logger.info("execute nums is " + nums);
						}
					} else {
						stopNums++;
						logger.info("this task is stop");
					}
				}
				if (stopNums == this.tasks.size()) {
					logger.info("the all task is stop");
					super.stop = true;
				}
				if (allExecuteNums == 0) {
					logger.info("all execute nums is 0 sleep 5 second");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					logger.info("all execute nums is " + allExecuteNums);
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				logger.info("tasks is empty stop the thread");
				super.stop = true;
			}
			
			
		}
		logger.info("end run task");

	}

}
