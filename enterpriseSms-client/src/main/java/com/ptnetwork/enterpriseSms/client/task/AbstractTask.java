/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.task;

/**
 * @author jasonzhang
 *
 */
public abstract class AbstractTask implements Task {
	protected boolean stop = false;
	private boolean running = false;

	/* (non-Javadoc)
	 * @see com.jason.ddoMsg.task.Task#execute()
	 */
	@Override
	public int execute() {
		this.running = true;
		int nums = executeTask();
		this.running = false;
		return nums;
	}
	
	protected abstract int executeTask();

	@Override
	public void start() {
		this.stop = false;
		
	}

	/* (non-Javadoc)
	 * @see com.jason.ddoMsg.task.Task#stop()
	 */
	@Override
	public void stop() {
		this.stop = true;

	}

	/* (non-Javadoc)
	 * @see com.jason.ddoMsg.task.Task#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public boolean isStop() {
		return this.stop;
	}
	
	

}
