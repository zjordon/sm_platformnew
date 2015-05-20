/**
 * Project Name:enterpriseSms-protocol
 * File Name:ExitRespPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����5:14:06
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;

/**
 * �˳�����Ļ�Ӧ��
 * ClassName:ExitRespPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 ����5:14:06 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class ExitRespPacket extends EmptyPacket {

	public final static int REQUEST_ID = MsgCommand.CMPP_TERMINATE_RESP;

	public ExitRespPacket(int sequenceId) {
		
		super(REQUEST_ID, sequenceId);
		
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

