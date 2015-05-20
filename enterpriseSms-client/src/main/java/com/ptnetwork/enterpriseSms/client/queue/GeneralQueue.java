/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author jasonzhang
 * @param <E>
 *
 */
public class GeneralQueue<E> extends ConcurrentLinkedQueue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<E> popTopElement(int num) {
		List<E> topList = new ArrayList<E>(num);
		for (int i=0; i<num; i++) {
			E element = super.poll();
			if (element != null) {
				topList.add(element);
			} else {
				break;
			}
		}
		return topList;
	}
}
