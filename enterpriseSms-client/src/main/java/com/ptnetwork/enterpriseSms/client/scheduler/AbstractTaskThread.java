/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.scheduler;

/**
 * @author jasonzhang
 *
 */
public abstract class AbstractTaskThread implements Runnable {

	protected boolean stop = false;
	
	public void start() {
		this.stop = false;
	}
	
	public void stop() {
		this.stop = true;
	}
	
	public boolean isStop() {
		return this.stop;
	}

	public abstract void run();
}
