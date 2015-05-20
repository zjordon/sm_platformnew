/**
 * Project Name:smPlatform
 * File Name:LoginException.java
 * Package Name:com.ptnetwork.enterpriseSms.client.core
 * Date:2012-11-27����7:58:58
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.exception;
/**
 * ��¼�쳣�ķ�װ
 * ClassName:LoginException <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2012-11-27 ����7:58:58 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class DeliverException extends Exception {

	/**
	 * serialVersionUID:TODO(��һ�仰����������ʾʲô).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = -4638659472375242572L;

	
	public DeliverException(String errMsg) {
		super(errMsg);
	}
}

