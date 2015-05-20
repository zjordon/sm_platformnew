/**
 * Project Name:enterpriseSms-client
 * File Name:AbstractSmsClient.java
 * Package Name:com.ptnetwork.enterpriseSms.client.net
 * Date:2013-1-27下午3:32:01
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.domain.Login;
import com.ptnetwork.enterpriseSms.client.exception.LoginException;
import com.ptnetwork.enterpriseSms.client.result.LoginResult;
import com.ptnetwork.enterpriseSms.protocol.ActiveTestRespPacket;
import com.ptnetwork.enterpriseSms.protocol.ExitPacket;
import com.ptnetwork.enterpriseSms.protocol.LoginPacket;
import com.ptnetwork.enterpriseSms.protocol.LoginRespPacket;
import com.ptnetwork.enterpriseSms.protocol.PacketHead;

/**
 * ClassName:AbstractSmsClient <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-27 下午3:32:01 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public abstract class AbstractSmsClient implements ConnectionInterface {

    private static Log log = LogFactory.getLog(AbstractSmsClient.class);
	
    protected Socket socket;
	protected InputStream in;
	protected OutputStream out;
	
	protected boolean stopReadData;
	
	protected long lastActiveTime;
	
	@Override
	public LoginResult login(Login login) throws LoginException {
		LoginResult loginResult = new LoginResult();
		
		log.info("mo login");
		try {
			this.socket = new Socket(login.getIp(), login.getPort());
			this.socket.setSoTimeout(300000);
			this.in = this.socket.getInputStream();
			this.out = this.socket.getOutputStream();
			this.stopReadData = false;
			this.startReadDataTask();
			LoginPacket loginPacket = new LoginPacket();
			loginPacket.setClientId(login.getUserName());
			loginPacket.setLoginMode((byte)login.getLoginMode());
			loginPacket.encodeAuthClient(login.getPassword());
			this.out.write(loginPacket.encode());
		} catch (UnknownHostException e) {
			
			log.error("login exception", e);
			throw new LoginException("login exception UnknownHostException");
			
		} catch (IOException e) {
			
			log.error("login exception", e);
			throw new LoginException("login exception IOException");
			
		}
		
		loginResult.setResultCode(LoginResult.LOGIN_SUCCESS);
		return loginResult;
	}

	@Override
	public void logout() {
		log.info("mo logout");
		if (this.out != null) {
			ExitPacket exitPacket = new ExitPacket();
			try {
				this.out.write(exitPacket.encode());
				this.disConnect();
			} catch (IOException e) {
				
				log.error("logout error", e);
				
			}
			
		}
		
		this.stopReadData = true;
		
		
		
	}

	@Override
	public void enableActiveTest() {
		
		// TODO Auto-generated method stub
		
	}
	
	protected abstract void processPacket(PacketHead packetHead);
	
	protected void processLoginResp(PacketHead packetHead) {
		log.info("receive lgoin resp packet");
		LoginRespPacket loginRespPacket = new LoginRespPacket(packetHead);
		try {
			loginRespPacket.decode(in);
		} catch (IOException e1) {
			
			log.error("loginRespPacket.decode error", e1);
			
		}
		if (loginRespPacket.getStatus() == 0) {
			//登录成功
			//TODO 验证是否是假包
			log.info("login success");
		} else {
			log.info("login fail with status is " + loginRespPacket.getStatus());
			//停止任务并关闭socket
			stopReadDataTask();
		    disConnect();
			
		}
	}
	
	protected void processActiveTest(PacketHead packetHead) throws IOException {
		log.info("receive active test packet");
		ActiveTestRespPacket packet = new ActiveTestRespPacket(packetHead.getSequenceId());
		this.out.write(packet.encode());
	}
	
	protected void disConnect() {
		try {
			if (this.in != null) {
				this.in.close();
				this.in = null;
			}
			if (this.out != null) {
				this.out.close();
				this.out = null;
			}
			if (this.socket != null) {
				this.socket.close();
				this.socket = null;
			}	
		} catch (IOException e) {
			log.error("disConnect error", e);
		}
		
	}
	
	private void startReadDataTask() {
		(new Thread(new ReadDataTask())).start();
	}
	
	protected void stopReadDataTask() {
		this.stopReadData = true;
	}
	
	private class ReadDataTask implements Runnable {

		@Override
		public void run() {
			log.info("start read data task now");
			while(!stopReadData) {
				PacketHead packetHead = new PacketHead();
				try {
					packetHead.decode(in);
				} catch (IOException e) {
					
					log.error("packetHead decode error", e);
					stopReadData = true;
					break;
					
				}
				lastActiveTime = System.currentTimeMillis();
				processPacket(packetHead);
			}
			log.info("end read data task");
			
		}
		
	}
}

