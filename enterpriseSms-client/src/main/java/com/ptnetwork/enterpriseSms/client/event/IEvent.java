/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.event;

/**
 * 事件基类接口
 * @author jasonzhang
 *
 */
public interface IEvent {

	void processEvent(String param) throws EventException;
}
