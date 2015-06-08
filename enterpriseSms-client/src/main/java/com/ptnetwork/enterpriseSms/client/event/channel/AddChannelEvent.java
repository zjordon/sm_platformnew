/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event.channel;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.domain.Channel;
import com.ptnetwork.enterpriseSms.client.event.AbstractEvent;
import com.ptnetwork.enterpriseSms.client.event.EventException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;

/**
 * @author jasonzhang
 *
 */
public class AddChannelEvent extends AbstractEvent {
	private static final Logger logger = Logger
			.getLogger(AddChannelEvent.class);

	/* (non-Javadoc)
	 * @see com.ptnetwork.enterpriseSms.client.event.AbstractEvent#processEvent(java.lang.String)
	 */
	@Override
	public void processEvent(String param) throws EventException {
		Map<String, String> paramMap = super.parseNecessaryParam(param, "id","postUrl");
		String id = paramMap.get("id");
		String postUrl = paramMap.get("postUrl");
		if (postUrl.length() > 128) {
			throw new EventException("postUrl is too long");
		} else if (StringUtils.isBlank(postUrl)) {
			throw new EventException("postUrl isBlank");
		}
		Channel channel = new Channel();
		channel.setId(id);
		channel.setPostUrl(postUrl);
		CacheManager.getInstance().updateChannelPostUrl(id, postUrl);
		try {
			DaoManager.getInstance().getChannelStore().saveChannel(channel);
		} catch (DaoException e) {
			logger.error("exception when processEvent", e);
			throw new EventException(e.getMessage());
		}

	}

}
