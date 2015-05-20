/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event.kernel;


import com.ptnetwork.enterpriseSms.client.core.Kernel;
import com.ptnetwork.enterpriseSms.client.event.AbstractEvent;
import com.ptnetwork.enterpriseSms.client.event.EventException;

/**
 * @author jasonzhang
 *
 */
public class StopKernelEvent extends AbstractEvent {

	/* (non-Javadoc)
	 * @see com.ptnetwork.enterpriseSms.client.event.AbstractEvent#processEvent(java.lang.String)
	 */
	@Override
	public void processEvent(String param) throws EventException {
		 Kernel.getInstance().stop();

	}

}
