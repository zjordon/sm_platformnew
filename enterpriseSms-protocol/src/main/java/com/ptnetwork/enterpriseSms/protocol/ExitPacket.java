/**
 * Project Name:enterpriseSms-protocol
 * File Name:ExitPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����5:01:35
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;

/**
 * �˳��
 * ClassName:ExitPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 ����5:01:35 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class ExitPacket extends EmptyPacket {
	
	public final static int REQUEST_ID = MsgCommand.CMPP_TERMINATE;
	
	public ExitPacket() {
		super(REQUEST_ID);
	}

	@Override
	public void decode(byte[] data) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRequestId() {
		return REQUEST_ID;
	}

}

