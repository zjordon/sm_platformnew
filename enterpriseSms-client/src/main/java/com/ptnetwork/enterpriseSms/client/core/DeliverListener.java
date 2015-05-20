/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.core;

import com.ptnetwork.enterpriseSms.protocol.DeliverPacket;

/**
 * @author jasonzhang
 *
 */
public interface DeliverListener {

	void addDeliver(DeliverPacket deliverPacket);
}
