/**
 * Project Name:enterpriseSms-protocol
 * File Name:EmptyPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����5:07:09
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * ClassName:EmptyPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-24 ����5:07:09 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public abstract class EmptyPacket extends AbstractPacket {

	public final static int PACKET_LENGTH = 12;//ֻ�а�ͷ����12
	
	public EmptyPacket(int requestId) {
		super(PACKET_LENGTH, requestId);
	}
	
	public EmptyPacket(int requestId, int sequenceId) {
		super(PACKET_LENGTH, requestId, sequenceId);
	}

	public EmptyPacket(PacketHead packetHead) {
		
		super(packetHead);
		
	}

	@Override
	public byte[] encode() {
		byte[] data = new byte[PACKET_LENGTH];
		super.encodePacketHead(data);
		return data;
	}

	@Override
	public void decode(InputStream in) throws IOException {
		
		//do nothing
		
	}
}

