/**
 * Project Name:smPlatform
 * File Name:MOSP.java
 * Package Name:com.ptnetwork.enterpriseSms.client.core
 * Date:2012-11-27锟斤拷锟斤拷8:21:49
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.core;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.ptnetwork.enterpriseSms.client.common.Sequence;
import com.ptnetwork.enterpriseSms.client.domain.Submit;
import com.ptnetwork.enterpriseSms.client.exception.SubmitException;
import com.ptnetwork.enterpriseSms.client.persistence.DbSubmitStore;
import com.ptnetwork.enterpriseSms.client.result.SubmitResult;
import com.ptnetwork.enterpriseSms.client.net.SubmitInterface;

/**
 * 
 * ClassName:MOSP <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2012-11-27 锟斤拷锟斤拷8:21:49 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class MOSP extends ServiceProvider {
	
	private static Log log = LogFactory.getLog(MOSP.class);
	
	private DbSubmitStore submitStore;
	
	private Sequence submitSequence;

	public void setSubmitStore(DbSubmitStore submitStore) {
		this.submitStore = submitStore;
	}
	
	

	public void setSubmitSequence(Sequence submitSequence) {
		this.submitSequence = submitSequence;
	}



	private boolean stopSubmit = false;

	private int processNums = 100;// 一锟斤拷锟皆达拷锟斤拷菘锟斤拷卸锟饺★拷募锟铰硷拷锟?

	@Override
	protected void beforeReLogin() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterLogin() {
		stopSubmit = false;
		
		ProcessMessageThread pmThread = new ProcessMessageThread();
		(new Thread(pmThread)).start();
//		ProcessResultThread prThread = new ProcessResultThread();
//		(new Thread(prThread)).start();
//		ProcessDeliverThread pdThread = new ProcessDeliverThread();
//		(new Thread(pdThread)).start();
	}

	@Override
	protected void afterExit() {
		log.info("mosp exit");
		// TODO Auto-generated method stub
		
	}
	
	private List<Submit> getMessages() {
		return submitStore.getSubmits(processNums,0,true);
	}
	
	private class ProcessMessageThread implements Runnable {

		public void run() {
//			submitStore.getSendings(processNums);
			log.info("process message thread start");
			while (!stopSubmit) {
				List<Submit> submits = getMessages();
				if (submits.isEmpty()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.error("interrrupt while thread sleep", e);
					}
				}
				for (int i = 0; i<submits.size(); i++) {
					Submit s = submits.get(i);
					s.setSequenceId(submitSequence.netxtId());
					doSendMessage(s);
				}
			}
			log.info("process message thread stop");

		}

	}
	
	//锟斤拷锟酵讹拷锟斤拷
	private void doSendMessage(Submit submit) {
		log.debug("send message to " + submit.getDestTermId());
		log.debug("sequenceId is " + submit.getSequenceId());
//		boolean success = false;
		Exception phse = null;
		SubmitResult submitResult = null;
		try {
			submitResult = ((SubmitInterface)super.smsClient).submit(submit);
			// success = resMessage(sendMessage(submit), submit);
		} catch (SubmitException e) {
			log.error("submit message with io exception");
			phse = e;
//			success = false;
		}
		
		if (phse != null) {
			// 锟结交锟斤拷锟斤拷锟斤拷息时锟斤拷锟斤拷锟届常
			submitStore.updateSubmitState(submit.getId(), 256, submit.getSequenceId());
			return;
		}
		
		// 删锟斤拷锟窖凤拷锟酵的讹拷锟斤拷.
		if (submitResult!= null && submitResult.getResultCode() == SubmitResult.SEND_SUCCESS) {
			//submitStore.deleteSubmit(submit.getId());
			submitStore.updateSubmitState(submit.getId(), 2, submit.getSequenceId());
			//submitStore.updateSubmitMsgId(submit.getId(), Integer.toString(submitResult.getSmId()));
		}
		
	}

}

