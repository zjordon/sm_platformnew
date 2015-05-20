/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.scheduler;

import com.ptnetwork.enterpriseSms.client.task.EventTask;
import com.ptnetwork.enterpriseSms.client.task.ReBillRequestTask;
import com.ptnetwork.enterpriseSms.client.task.ReceiveMsgTask;
import com.ptnetwork.enterpriseSms.client.task.SaveBillRequestTask;
import com.ptnetwork.enterpriseSms.client.task.SaveDeliverTask;

/**
 * 任务调度类 处理任务的启动，停止，并行等逻揖
 * @author jasonzhang
 *
 */
public class TaskScheduler {

private final static TaskScheduler instance = new TaskScheduler();
	
	private TaskScheduler(){}
	
	public final static TaskScheduler getInstance() {
		return instance;
	}
	
	private ReceiveMsgTask receiveMsgTask;
	private SaveDeliverTask saveDeliverTask;
	private SaveBillRequestTask saveBillRequestTask;
	private ReBillRequestTask reBillRequestTask;
	private EventTask eventTask;
	
	private SingleTaskThread recevieMsgThread;
	private MultileTaskThread multileTaskThread;
	public void init() {
		this.receiveMsgTask = new ReceiveMsgTask();
		this.recevieMsgThread = new SingleTaskThread(this.receiveMsgTask);
		
		this.saveDeliverTask = new SaveDeliverTask();
		this.saveBillRequestTask = new SaveBillRequestTask();
		this.reBillRequestTask = new ReBillRequestTask();
		this.eventTask = new EventTask();
		this.multileTaskThread = new MultileTaskThread();
		this.multileTaskThread.addTask(saveDeliverTask);
		this.multileTaskThread.addTask(saveBillRequestTask);
		this.multileTaskThread.addTask(reBillRequestTask);
		this.multileTaskThread.addTask(eventTask);
		this.startAllTask();
		
	}
	
	public void startAllTask() {
		ThreadUCExceptionHandler exceptionHandler = new ThreadUCExceptionHandler();
		Thread recevieMsgThreadWrapper = new Thread(this.recevieMsgThread);
		recevieMsgThreadWrapper.setUncaughtExceptionHandler(exceptionHandler);
		recevieMsgThreadWrapper.start();
		Thread threadWrapper = new Thread(multileTaskThread);
		threadWrapper.setUncaughtExceptionHandler(exceptionHandler);
		threadWrapper.start();
	}
}
