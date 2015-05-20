/**
 * Project Name:enterpriseSms-protocol
 * File Name:LoginRespPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-24����5:16:26
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * SP��SMGW��¼�Ļ�Ӧ ClassName:LoginRespPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2013-1-24 ����5:16:26 <br/>
 * 
 * @author JasonZhang
 * @version
 * @since JDK 1.6
 * @see
 */
public class LoginRespPacket extends AbstractPacket {

	public final static int REQUEST_ID = 0x80000001;

	// ��ĳ���
	// 12(消息头) + 1(Status) + 16(AuthenticatorServer) +1(Version)
	public final static int PACKET_LENGTH = 30;

	public LoginRespPacket(int sequenceId) {
		super(PACKET_LENGTH, REQUEST_ID, sequenceId);
	}

	public LoginRespPacket(PacketHead packetHead) {
		super(packetHead);
	}
	
	private byte status;//Login���󷵻ؽ��
	//������˷��ظ�ͻ��˵���֤�룬���ͻ�����֤���ʱ������Ϊ�ա�
    //��ֵͨ����MD5 hash����ó��ʾ���£�
    //AuthenticatorServer��MD5(Status+AuthenticatorClient+shared secret )
	//Share secret �ɷ������ͻ��������̶����15�ֽڣ�
    //AuthenticatorClientΪ�ͻ��˷��͸����˵���һ����Ϣlogin�е�ֵ��
	private String authenticatorServer;
	
	private byte version;
	

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	@Override
	public byte[] encode() {
		
		byte[] data = new byte[PACKET_LENGTH];
		super.encodePacketHead(data);
		Utils.int2bytes(this.status, data, 12);
		if (this.authenticatorServer != null) {
			byte [] bytes = this.authenticatorServer.getBytes();
			
			Utils.string2bytes(this.authenticatorServer, data, 16, 16);
		} else {
			for (int i=16; i<32; i++) {
				data[i] = 0x00;
			}
		}
		
		data[32] = this.version;
		return data;
	}

	@Override
	public void decode(InputStream in) throws IOException {
		
		this.status = (byte)in.read();
		this.authenticatorServer = Utils.bytes2string(in, 16);
		//TODO ��ݹ�������authenticatorServer�еľ�����Ϣ����ȽϷ�ֹ����
		this.version = (byte)in.read();
		
	}
	
	public void decode(byte[] data) throws IOException {
		this.status = data[12];
		this.authenticatorServer = Utils.bytes2string(data, 13, 16);
		this.version = data[29];
	}
	
	public void encodeAuthServer(String authClient, String sharedsecret) {
		//TODO ��ݹ��������֤��,=MD5��ClientID+7 �ֽڵĶ�����0��0x00�� +shared secret+timestamp��
		this.authenticatorServer = authClient + sharedsecret;
	}
	
	public String decodeAuthServer() {
		//TODO ��ݹ����������˷����ַ��authClient��sharedsecret
		return this.authenticatorServer;
	}

	@Override
	public int getRequestId() {
		return REQUEST_ID;
	} 
}
