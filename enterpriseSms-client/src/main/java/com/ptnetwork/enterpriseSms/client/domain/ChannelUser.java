/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.domain;

/**
 * 渠道用户
 * @author jasonzhang
 *
 */
public class ChannelUser {

	private String id;
	private String username;
	private String password;
	private String channelId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	
}
