/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.core;

/**
 * 配置参数
 * @author jasonzhang
 *
 */
public class Config {

	private String ismgIp;
	private int ismgPort;
	private String spId;
	private String sharedSecret;
	private String spCode;
	private int activeTestTimeout;
	private int activeTestInterval;
	private int activeTesCount;
	private int connectCount;
	public String getIsmgIp() {
		return ismgIp;
	}
	public void setIsmgIp(String ismgIp) {
		this.ismgIp = ismgIp;
	}
	public int getIsmgPort() {
		return ismgPort;
	}
	public void setIsmgPort(int ismgPort) {
		this.ismgPort = ismgPort;
	}
	public String getSpId() {
		return spId;
	}
	public void setSpId(String spId) {
		this.spId = spId;
	}
	public String getSharedSecret() {
		return sharedSecret;
	}
	public void setSharedSecret(String sharedSecret) {
		this.sharedSecret = sharedSecret;
	}
	public String getSpCode() {
		return spCode;
	}
	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}
	public int getActiveTestTimeout() {
		return activeTestTimeout;
	}
	public void setActiveTestTimeout(int activeTestTimeout) {
		this.activeTestTimeout = activeTestTimeout;
	}
	public int getActiveTestInterval() {
		return activeTestInterval;
	}
	public void setActiveTestInterval(int activeTestInterval) {
		this.activeTestInterval = activeTestInterval;
	}
	public int getActiveTesCount() {
		return activeTesCount;
	}
	public void setActiveTesCount(int activeTesCount) {
		this.activeTesCount = activeTesCount;
	}
	public int getConnectCount() {
		return connectCount;
	}
	public void setConnectCount(int connectCount) {
		this.connectCount = connectCount;
	}
	
	
}
