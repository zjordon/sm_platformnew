/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.event.channelInstruct.AddChannelInstructEvent;
import com.ptnetwork.enterpriseSms.client.event.channelInstruct.DeleteChannelInstructEvent;
import com.ptnetwork.enterpriseSms.client.event.channelInstruct.UpdateChanelInstruct;
import com.ptnetwork.enterpriseSms.client.event.channelUser.AddChannelUserEvent;
import com.ptnetwork.enterpriseSms.client.event.channelUser.DeleteChannelUserEvent;
import com.ptnetwork.enterpriseSms.client.event.channelUser.UpdateCUPasswordEvent;
import com.ptnetwork.enterpriseSms.client.event.kernel.StopKernelEvent;

/**
 * 事件管理器
 * @author jasonzhang
 *
 */
public class EventManager {

	private static final Logger logger = Logger
			.getLogger(EventManager.class);
	
	private final static EventManager instance = new EventManager();
	
	private Map<String, IEvent> eventMap;
	
	private EventManager(){}
	
	public final static EventManager getInstance() {
		return instance;
	}
	/**
	 * 初始化
	 */
	public void init() {
		eventMap = new HashMap<String, IEvent>();
		eventMap.put("AddChannelUserEvent", new AddChannelUserEvent());
		eventMap.put("DeleteChannelUserEvent", new DeleteChannelUserEvent());
		eventMap.put("UpdateCUPasswordEvent", new UpdateCUPasswordEvent());
		eventMap.put("UpdateCBInstruct", new UpdateChanelInstruct());
		eventMap.put("AddChannelBusinessEvent", new AddChannelInstructEvent());
		eventMap.put("DeleteChannelBusinessEvent", new DeleteChannelInstructEvent());
		eventMap.put("StopKernelEvent", new StopKernelEvent());
		
	}
	
	
	/**
	 * 执行事件
	 * @param eventId
	 * @param param
	 * @throws EventException
	 */
	public void executeEvent(String eventId, String param) throws EventException {
		IEvent event = this.eventMap.get(eventId);
		if (event != null) {
			event.processEvent(param);
		} else {
			throw new EventException("the event is not exists with id " + eventId);
		}
	}
	
	public void destory() {
		this.eventMap.clear();
	}
}
