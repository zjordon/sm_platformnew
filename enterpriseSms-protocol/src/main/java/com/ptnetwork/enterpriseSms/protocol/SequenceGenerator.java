/**
 * Project Name:enterpriseSms-protocol
 * File Name:SequenceGenerator.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-23����10:34:50
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;
/**
 * sequence�����,ȡֵ0x00000000��0xFFFFFFFF
 * ClassName:SequenceGenerator <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-23 ����10:34:50 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class SequenceGenerator {
	
	private int sequence = 0;

	private SequenceGenerator(){}
	private final static SequenceGenerator instance = new SequenceGenerator();
	
	public static SequenceGenerator getInstance() {
		return instance;
	}
	
	public int getSequence() {
		++sequence;
		if(sequence>255){
			sequence=0;
		}
		return sequence;
	}
}

