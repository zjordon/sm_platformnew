/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event.channelInstruct;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.domain.ChannelInstruct;
import com.ptnetwork.enterpriseSms.client.event.AbstractEvent;
import com.ptnetwork.enterpriseSms.client.event.EventException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;

/**
 * 新增渠道业务
 * @author jasonzhang
 *
 */
public class AddChannelInstructEvent extends AbstractEvent {
	private static final Logger logger = Logger
			.getLogger(AddChannelInstructEvent.class);

	/* (non-Javadoc)
	 * @see com.jason.ddoMsg.event.AbstractEvent#processEvent(java.lang.String, java.lang.String)
	 */
	@Override
	public void processEvent(String param)
			throws EventException {
		Map<String, String> paramMap = super.parseNecessaryParam(param, "id","instruct","channelId");
		String id = paramMap.get("id");
		String instruct = paramMap.get("instruct");
		String channelId = paramMap.get("channelId");
		
		ChannelInstruct channelInstruct = new ChannelInstruct();
		channelInstruct.setId(id);
		channelInstruct.setInstruct(instruct);
		channelInstruct.setChannelId(channelId);
		
		CacheManager.getInstance().addChannelInstruct(channelInstruct);
		try {
			DaoManager.getInstance().getChannelInstructStore().saveChannelInstruct(channelInstruct);
		} catch (DaoException e) {
			logger.error("exception when processEvent", e);
			throw new EventException(e.getMessage());
		}

	}

}
