/**
 * Project Name:enterpriseSms-protocol
 * File Name:ActiveTestRespPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����5:12:41
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * ����ͨ��t·�Ƿ���Ļ�Ӧ��
 * ClassName:ActiveTestRespPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 ����5:12:41 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class ActiveTestRespPacket extends AbstractPacket {
	
	public final static int REQUEST_ID = MsgCommand.CMPP_ACTIVE_TEST_RESP;
	
	public final static int PACKET_LENGTH = 13;
	
	private byte reserved = (byte)0;

	public ActiveTestRespPacket(int sequenceId) {
		
		super(PACKET_LENGTH, REQUEST_ID, sequenceId);
		
	}

	@Override
	public byte[] encode() {
		byte[] data = new byte[PACKET_LENGTH];
		super.encodePacketHead(data);
		data[12] = reserved;
		return data;
	}

	@Override
	public void decode(InputStream in) throws IOException {
		// TODO Auto-generated method stub
		
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

