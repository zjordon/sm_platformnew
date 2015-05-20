/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event.channelInstruct;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.event.AbstractEvent;
import com.ptnetwork.enterpriseSms.client.event.EventException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;

/**
 * 删除渠道业务
 * @author jasonzhang
 *
 */
public class DeleteChannelInstructEvent extends AbstractEvent {
	private static final Logger logger = Logger
			.getLogger(DeleteChannelInstructEvent.class);

	/* (non-Javadoc)
	 * @see com.jason.ddoMsg.event.AbstractEvent#processEvent(java.lang.String, java.lang.String)
	 */
	@Override
	public void processEvent(String param)
			throws EventException {
		Map<String, String> paramMap = super.parseNecessaryParam(param, "id");
		String id = paramMap.get("id");
		CacheManager.getInstance().deleteChannelInstruct(id);
		try {
			DaoManager.getInstance().getChannelInstructStore().deleteChannelInstruct(id);
		} catch (DaoException e) {
			logger.error("exception when processEvent", e);
			throw new EventException(e.getMessage());
		}

	}

}
