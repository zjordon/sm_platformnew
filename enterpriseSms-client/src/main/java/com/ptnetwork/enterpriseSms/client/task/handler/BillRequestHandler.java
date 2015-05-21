/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.task.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.domain.BillRequest;
import com.ptnetwork.enterpriseSms.client.tools.HttpHelper;
import com.ptnetwork.enterpriseSms.client.tools.HttpRequestResponse;

/**
 * 
 * @author jasonzhang
 *
 */
public class BillRequestHandler {
	private static final Logger logger = Logger.getLogger(BillRequestHandler.class);
	
	private final static BillRequestHandler instance = new BillRequestHandler();
	
	public final static BillRequestHandler getInstance() {
		return instance;
	}
	
	private BillRequestHandler(){}

	public void handle(BillRequest billRequest) throws HandlerException {
		HttpRequestResponse response = null;
		String responseState = null;
		try {
			response = this.sendBillRequest(billRequest);
		} catch (ClientProtocolException e) {
			logger.error("exception happen when handle", e);
			responseState = e.getMessage();
		} catch (IOException e) {
			logger.error("exception happen when handle", e);
			responseState = e.getMessage();
		}
		if (response != null) {
			responseState = response.getMsg().trim();
			if (responseState.startsWith("1,")) {
				billRequest.setRepeatFlag(false);
			} else {
				//判断是否是数字
				if (!NumberUtils.isDigits(responseState)) {
					//返回的不是数字，算发送失败，需要重发
					billRequest.setRepeatFlag(true);
				} else if ("500".equals(responseState)){
					//如果是数字并且是500表明系统正在维护中，需要重发
					billRequest.setRepeatFlag(true);
				} else {
					//其它情况都不需要重发
					billRequest.setRepeatFlag(false);
				}
			}
		} else {
			billRequest.setRepeatFlag(true);
		}
		if (responseState != null) {
			billRequest.setResponseState(responseState.length() > 64 ? responseState.substring(0, 63) : responseState);
		}
		billRequest.setState(1);
	}
	
	private HttpRequestResponse sendBillRequest(BillRequest billRequest) throws ClientProtocolException, IOException {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", billRequest.getUserName());
		paramMap.put("password", billRequest.getUserPass());
		paramMap.put("mobile", Long.toString(billRequest.getMsisdn()));
		paramMap.put("content", billRequest.getInstruct());
		HttpRequestResponse response = null;
		String postUrl = CacheManager.getInstance().getBillRequestUrl();
		
		response = HttpHelper.getInstnace().post(postUrl, paramMap);
		
		return response;
	}
}
