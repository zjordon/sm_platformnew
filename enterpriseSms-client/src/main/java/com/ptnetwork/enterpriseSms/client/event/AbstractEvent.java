/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jasonzhang
 *
 */
public abstract class AbstractEvent implements IEvent {

	/* (non-Javadoc)
	 * @see com.jason.ddoMsg.event.Event#processEvent(java.lang.String, java.lang.String)
	 */
	@Override
	public abstract void processEvent(String param)
			throws EventException;
	
	protected Map<String, String> parseNecessaryParam(String param, String... neccaryParams) throws ParseParamException {
		if (StringUtils.isBlank(param)) {
			throw new ParseParamException("the param is empty!");
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] paramKeyValues = StringUtils.split(param, ',');
		int idx = -1;
		for (String paramKeyValue : paramKeyValues) {
			idx = paramKeyValue.indexOf(':');
			if (idx == -1) {
				throw new ParseParamException("the param miss colon, it must like 'key:value'");
			}
			if (paramKeyValue.length() > idx) {
				map.put(paramKeyValue.substring(0, idx).trim(), paramKeyValue.substring(idx + 1).trim());
			}
		}
		for (String neccaryParam : neccaryParams) {
			if (map.get(neccaryParam) == null) {
				throw new ParseParamException("miss the neccary param " + neccaryParam);
			}
		}
		return map;
	}
	
	protected void validDigital(String s) throws ParseParamException {
		if (!this.isDigital(s)) {
			throw new ParseParamException("the param value is not digital");
		}
	}
	
	protected void validState(int num) throws ParseParamException {
		if (!this.isStateParam(num)) {
			throw new ParseParamException("the param value is not 0 or 1");
		}
	}
	
	private boolean isDigital(String s) {
		char[] cs = s.toCharArray();
		for (char c : cs) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isStateParam(int num) {
		if (num !=0 && num != 1) {
			return false;
		}
		return true;
	}

}
