/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event.channelUser;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.event.AbstractEvent;
import com.ptnetwork.enterpriseSms.client.event.EventException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;

/**
 * 更新渠道用户密码
 * @author jasonzhang
 *
 */
public class UpdateCUPasswordEvent extends AbstractEvent {
	private static final Logger logger = Logger
			.getLogger(UpdateCUPasswordEvent.class);

	/* (non-Javadoc)
	 * @see com.jason.ddoMsg.event.AbstractEvent#processEvent(java.lang.String, java.lang.String)
	 */
	@Override
	public void processEvent(String param)
			throws EventException {
		Map<String, String> paramMap = super.parseNecessaryParam(param, "id",  "password");
		String id = paramMap.get("id");
		String password = paramMap.get("password");
		CacheManager.getInstance().updateChannelUserInfo(id, password);
		try {
			DaoManager.getInstance().getChannelUserStore().updatChannelUserInfo(id, password);
		} catch (DaoException e) {
			logger.error("exception when processEvent", e);
			throw new EventException(e.getMessage());
		}

	}

}
