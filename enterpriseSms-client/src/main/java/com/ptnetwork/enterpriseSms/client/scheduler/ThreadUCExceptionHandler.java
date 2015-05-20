/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.scheduler;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

/**
 * 捕获纯种线程中不呆预知的异常，记录日志
 * @author jasonzhang
 *
 */
public class ThreadUCExceptionHandler implements UncaughtExceptionHandler {
	private static final Logger logger = Logger.getLogger(ThreadUCExceptionHandler.class);

	/* (non-Javadoc)
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.info("exception when the thread run", e);

	}

}
