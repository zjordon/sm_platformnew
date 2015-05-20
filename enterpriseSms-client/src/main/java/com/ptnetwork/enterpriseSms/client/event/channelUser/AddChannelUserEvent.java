/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event.channelUser;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.domain.ChannelUser;
import com.ptnetwork.enterpriseSms.client.event.AbstractEvent;
import com.ptnetwork.enterpriseSms.client.event.EventException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;

/**
 * 新增渠道
 * 
 * @author jasonzhang
 *
 */
public class AddChannelUserEvent extends AbstractEvent {
	private static final Logger logger = Logger
			.getLogger(AddChannelUserEvent.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jason.ddoMsg.event.AbstractEvent#processEvent(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void processEvent(String param)
			throws EventException {
		Map<String, String> paramMap = super.parseNecessaryParam(param, "id","username", "password", "channelId");
		String id = paramMap.get("id");
		String username = paramMap.get("username");
		String password = paramMap.get("password");
		String channelId = paramMap.get("channelId");
		
		ChannelUser channelUser = new ChannelUser();
		channelUser.setId(id);
		channelUser.setChannelId(channelId);
		channelUser.setUsername(username);
		channelUser.setPassword(password);
		CacheManager.getInstance().addChannelUser(channelUser);
		try {
			DaoManager.getInstance().getChannelUserStore().saveChannelUser(channelUser);
		} catch (DaoException e) {
			logger.error("exception when processEvent", e);
			throw new EventException(e.getMessage());
		}
		

	}

}
