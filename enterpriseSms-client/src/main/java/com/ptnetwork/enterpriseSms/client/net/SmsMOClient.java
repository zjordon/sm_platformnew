/**
 * Project Name:enterpriseSms-client
 * File Name:SmsMOClient.java
 * Package Name:com.ptnetwork.enterpriseSms.client.net
 * Date:2013-1-27下午2:14:22
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.domain.Login;
import com.ptnetwork.enterpriseSms.client.domain.Submit;
import com.ptnetwork.enterpriseSms.client.exception.LoginException;
import com.ptnetwork.enterpriseSms.client.exception.SubmitException;
import com.ptnetwork.enterpriseSms.client.result.LoginResult;
import com.ptnetwork.enterpriseSms.client.result.SubmitResult;
import com.ptnetwork.enterpriseSms.protocol.ExitPacket;
import com.ptnetwork.enterpriseSms.protocol.LoginPacket;
import com.ptnetwork.enterpriseSms.protocol.LoginRespPacket;
import com.ptnetwork.enterpriseSms.protocol.PacketHead;
import com.ptnetwork.enterpriseSms.protocol.SubmitRespPacket;

/**
 * ClassName:SmsMOClient <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-1-27 下午2:14:22 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class SmsMOClient extends AbstractSmsClient implements SubmitInterface {
	private static Log log = LogFactory.getLog(SmsMOClient.class);

	@Override
	public SubmitResult submit(Submit submit) throws SubmitException {
		SubmitResult submitResult = new SubmitResult();
		this.lastActiveTime = System.currentTimeMillis();
		log.info("submit a message");
		try {
			this.out.write(submit.getSubmitPacket().encode());
			submitResult.setResultCode(SubmitResult.SEND_SUCCESS);
		} catch (IOException e) {
			
			log.error("submit error", e);
			submitResult.setResultCode(SubmitResult.SEND_FAIL);
			
		}
		return submitResult;
	}

	@Override
	public SubmitResult batchSubmit(List<Submit> submits)
			throws SubmitException {
		SubmitResult submitResult = new SubmitResult();
		// TODO Auto-generated method stub
		log.info("submit batch message");
		return submitResult;
	}

	@Override
	protected void processPacket(PacketHead packetHead) {
		
		switch (packetHead.getRequestId()) {
		case 0x80000001:
			//登录响应包
			processLoginResp(packetHead);
			break;
		case 0x80000002:
			//上行消息响应包
			try {
				this.processSubmitResp(packetHead);
			} catch (IOException e) {
				log.error("processSubmitResp error", e);
				stopReadDataTask();
				disConnect();
				
			}
			break;
		case 0x00000004:
			//链路测试请求包
			try {
				processActiveTest(packetHead);
			} catch (IOException e) {
				
				log.error("process active test error", e);
				stopReadDataTask();
				disConnect();
				
			}
			break;
		case 0x80000004:
			//链路测试响应包
			break;
		case 0x80000006:
			//退出响应包
			break;
		default:
			//其它包为异常情况，直接断开连接
			log.info("requestid is not excepted, requestid is " + packetHead.getRequestId());
			stopReadDataTask();
			disConnect();
			
			break;
		}
		
	}
	
	private void processSubmitResp(PacketHead packetHead) throws IOException {
		SubmitRespPacket submitRespPacket = new SubmitRespPacket(packetHead);
		submitRespPacket.decode(in);
		log.info("submit resp status is " + submitRespPacket.getStatus());
		//TODO 加入消息处理队列
	}

}

