/**
 * Project Name:enterpriseSms-protocol
 * File Name:ActiveTestPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����5:10:39
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;

/**
 * ����ͨ��t·�Ƿ���������l������˷���SP ��SMGW ����ͨ��ʱ���ʹ�����4ά��l�ӣ�
 * ClassName:ActiveTestPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2013-1-24 ����5:10:39 <br/>
 * 
 * @author JasonZhang
 * @version
 * @since JDK 1.6
 * @see
 */
public class ActiveTestPacket extends EmptyPacket {

	public final static int REQUEST_ID = MsgCommand.CMPP_ACTIVE_TEST;

	public ActiveTestPacket(int sequenceId) {

		super(REQUEST_ID, sequenceId);

	}

	public ActiveTestPacket() {

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
