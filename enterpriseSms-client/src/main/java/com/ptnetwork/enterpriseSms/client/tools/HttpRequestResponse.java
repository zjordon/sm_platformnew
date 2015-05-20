/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.tools;

/**
 * 下发请求结果
 * @author jasonzhang
 *
 */
public class HttpRequestResponse {

	private int statusCode;
	private String msg;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
