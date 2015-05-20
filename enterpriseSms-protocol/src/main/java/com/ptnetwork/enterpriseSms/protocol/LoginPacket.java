/**
 * Project Name:enterpriseSms-protocol
 * File Name:LoginPacket.java
 * Package Name:com.ptnetwork.enterpriseSms.protocol
 * Date:2013-1-23����10:14:12
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ��¼��
 * ClassName:LoginPacket <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-23 ����10:14:12 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class LoginPacket extends AbstractPacket {
	public final static int REQUEST_ID = MsgCommand.CMPP_CONNECT;
	public final static int PACKET_LENGTH = 39;
	//12(��ͷ����) + 6(clientID) + 16(AuthenticatorClient) + 4(TimeStamp) + 1(Version)
	
	
	public LoginPacket() {
		super(PACKET_LENGTH, REQUEST_ID);
		
	}
	
	public LoginPacket(PacketHead packetHead) {
		super(packetHead);
		
	}
	
	private String clientId;//�ͻ���id
	private byte[] authenticatorClient;//�ͻ���������¼�������˵���֤��
	private byte loginMode;//��¼���ͣ�0�����Ͷ���Ϣ��1�����ն���Ϣ,2���շ�����Ϣ,��������
	private int timestamp;//ʱ���������,�ɿͻ��˲����ʽΪ��MMDDHHMMSS,������ʱ���룬10λ���ֵ����ͣ��Ҷ��롣����0301000000��ת��Ϊ������Ϊ11F0E540��
	private byte version;//�ͻ���֧�ֵİ汾�ţ���λ4bit��ʾ���汾�ţ���λ4bit��ʾ�ΰ汾�ţ�����0x20��ʾ2.0�汾��


	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public byte getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(byte loginMode) {
		this.loginMode = loginMode;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	public byte[] getAuthenticatorClient() {
		return authenticatorClient;
	}

	@Override
	public byte[] encode() {
		byte[] data = new byte[PACKET_LENGTH];
		super.encodePacketHead(data);
		Utils.string2bytes(this.clientId, data, 12, 6);
		//Utils.string2bytes(this.authenticatorClient, data, 20, 16);
		int idx = 0;
		for (int i=18; i<34; i++) {
			data[i] = this.authenticatorClient[idx++];
		}
		//data[36] = this.loginMode;
		data[34] = this.version;
		Utils.int2bytes(this.timestamp, data, 35);
		//data[40] = this.version;
		return data;
	}
	@Override
	public void decode(InputStream in) throws IOException {
		
		this.clientId = Utils.bytes2string(in, 8);
		//this.authenticatorClient = Utils.bytes2string(in, 16);
		this.authenticatorClient = new byte[16];
		in.read(this.authenticatorClient);
		this.loginMode = (byte) in.read();
		this.timestamp = Utils.bytes2int(in);
		this.version = (byte) in.read();
		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("clientId is ").append(this.clientId);
		builder.append(" authClient is ").append(this.authenticatorClient);
		builder.append(" loginMode is ").append(this.loginMode);
		return builder.toString();
	}

	public void encodeAuthClient(String sharedsecret) {
		try {
			MessageDigest md5=MessageDigest.getInstance("MD5");
			byte[] data=(this.clientId+"\0\0\0\0\0\0\0\0\0"+sharedsecret+ this.timestamp).getBytes();
			this.authenticatorClient =  md5.digest(data);
		} catch (NoSuchAlgorithmException e) {
			//log.error("SP链接到ISMG拼接AuthenticatorSource失败："+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public byte[] decodeAuthClient() {
		//TODO ��ݹ������������
		return this.authenticatorClient;
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

