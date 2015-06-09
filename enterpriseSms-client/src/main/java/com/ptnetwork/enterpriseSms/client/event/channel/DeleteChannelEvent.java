/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event.channel;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.event.AbstractEvent;
import com.ptnetwork.enterpriseSms.client.event.EventException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;

/**
 * @author jasonzhang
 *
 */
public class DeleteChannelEvent extends AbstractEvent {
	private static final Logger logger = Logger
			.getLogger(DeleteChannelEvent.class);

	/* (non-Javadoc)
	 * @see com.ptnetwork.enterpriseSms.client.event.AbstractEvent#processEvent(java.lang.String)
	 */
	@Override
	public void processEvent(String param) throws EventException {
		Map<String, String> paramMap = super.parseNecessaryParam(param, "id");
		String id = paramMap.get("id");
		try {
			DaoManager.getInstance().getChannelStore().deleteChannel(id);
			CacheManager.getInstance().deleteChannelPostUrl(id);
		} catch (DaoException e) {
			logger.error("exception when processEvent", e);
			throw new EventException(e.getMessage());
		}

	}

}
