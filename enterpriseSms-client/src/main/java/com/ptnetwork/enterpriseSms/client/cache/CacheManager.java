/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.domain.Channel;
import com.ptnetwork.enterpriseSms.client.domain.ChannelInstruct;
import com.ptnetwork.enterpriseSms.client.domain.ChannelUser;
import com.ptnetwork.enterpriseSms.client.persistence.DaoException;
import com.ptnetwork.enterpriseSms.client.persistence.DaoManager;
import com.ptnetwork.enterpriseSms.client.tools.PropertiesHelper;

/**
 * @author jasonzhang
 *
 */
public class CacheManager {
	private static Log log = LogFactory.getLog(CacheManager.class);

	private final static CacheManager instance = new CacheManager();

	public final static CacheManager getInstance() {
		return instance;
	}

	private Map<String, ChannelUser> channelUserMap = new HashMap<String, ChannelUser>();
	private Map<String, ChannelInstruct> channelInstructMap = new HashMap<String, ChannelInstruct>();
	private Map<String, String> postUrlMap = new HashMap<String, String>();
	private String billRequestUrl;

	private CacheManager() {
	}

	public void init() {
		List<ChannelUser> channelUserList = null;
		List<ChannelInstruct> channelInstructList = null;
		List<Channel> channelList = null;
		try {
			channelUserList = DaoManager.getInstance().getChannelUserStore()
					.getChannelUserList();
			channelInstructList = DaoManager.getInstance()
					.getChannelInstructStore().getChannelInstructList();
			channelList = DaoManager.getInstance().getChannelStore().getChannelList();
		} catch (DaoException e) {
			log.error("exception happen when init", e);
		}
		if (channelUserList != null && !channelUserList.isEmpty()) {
			for (ChannelUser channelUser : channelUserList) {
				if (!this.channelUserMap.containsKey(channelUser.getChannelId())) {
					this.channelUserMap.put(channelUser.getChannelId(),
							channelUser);
				}

			}
		}
		if (channelInstructList != null && !channelInstructList.isEmpty()) {
			for (ChannelInstruct channelInstruct : channelInstructList) {
				this.channelInstructMap.put(channelInstruct.getInstruct(),
						channelInstruct);
			}
		}
		if (channelList != null && !channelList.isEmpty()) {
			for (Channel channel : channelList) {
				this.postUrlMap.put(channel.getId(), channel.getPostUrl());
			}
		}
		Properties props = PropertiesHelper.getInstance().loadProps(
				"config/setting.properties", "setting.properties");
		if (props != null && !props.isEmpty()) {
			this.billRequestUrl = props.getProperty("billRequestUrl");
		}
	}

	public String getBillRequestUrl() {
		return this.billRequestUrl;
	}

	public ChannelUser getChannelUser(String instruct) {
		ChannelInstruct channelInstruct = this.channelInstructMap.get(instruct);
		if (channelInstruct != null) {
			return this.channelUserMap.get(channelInstruct.getChannelId());
		}
		return null;
	}
	
	public ChannelInstruct getChannelInstruct(String instruct) {
		return this.channelInstructMap.get(instruct);
	}

	public void addChannelUser(ChannelUser channelUser) {
		this.channelUserMap.put(channelUser.getChannelId(), channelUser);
	}

	public void updateChannelUserInfo(String id, String password) {
		ChannelUser channelUser = this.channelUserMap.get(id);
		if (channelUser != null) {
			channelUser.setPassword(password);
		}
	}
	
	public void deleteChannelUser(String id) {
		if (this.channelUserMap.containsKey(id)) {
			this.channelUserMap.remove(id);
		}
	}
	
	public void addChannelInstruct(ChannelInstruct channelInstruct) {
		this.channelInstructMap.put(channelInstruct.getInstruct(), channelInstruct);
	}

	public void updateChannelInstruct(String id, String instruct) {
		String findKey = null;
		ChannelInstruct findValue = null;
		for (Map.Entry<String, ChannelInstruct> entry : this.channelInstructMap
				.entrySet()) {

			if (entry.getValue().getId().equals(id)) {
				findKey = entry.getKey();
				findValue = entry.getValue();
				break;
			}

		}
		if (findKey != null) {
			findValue.setInstruct(instruct);
			this.channelInstructMap.remove(findKey);
			this.channelInstructMap.put(instruct, findValue);
		}
	}
	
	public void updateChannelPostUrl(String channelId, String postUrl) {
		this.postUrlMap.put(channelId, postUrl);
	}
	
	public void deleteChannelPostUrl(String channelId) {
		this.postUrlMap.remove(channelId);
	}
	
	public String getPostUrl(String channelId) {
		return this.postUrlMap.get(channelId);
	}
	
	public void deleteChannelInstruct(String id) {
		if (this.channelInstructMap.containsKey(id)) {
			this.channelInstructMap.remove(id);
		}
	}

	
}
