/**
 * Project Name:enterpriseSms-client
 * File Name:FileSequence.java
 * Package Name:com.ptnetwork.enterpriseSms.client.common
 * Date:2013-2-9下午4:52:40
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.common;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.tools.PropertiesHelper;

/**
 * ClassName:FileSequence <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-2-9 下午4:52:40 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class FileSequence extends AbstractSequence {
	private static Log log = LogFactory.getLog(FileSequence.class);
	
	private int blockSize = 20;
	private int cacheId;
	
	public FileSequence() {
		currentId = minId;
		cacheId = currentId;
	}

	@Override
	public int netxtId() {
		if (currentId >= cacheId) {
			this.getNextBlock();
		}
		int id = currentId;
		currentId++;
		return id;
	}
	
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	
	private void getNextBlock() {
		PropertiesHelper helper = PropertiesHelper.getInstance();
		Properties props = helper.loadProps("config/sequence.properties", "sequence.properties");
		if (props != null) {
			String sequenceId = props.getProperty(super.sequenceName);
			if (sequenceId != null) {
				this.currentId = Integer.parseInt(sequenceId);
			}
			
		} else {
			log.warn("sequence file is not exists!");
		}
		this.cacheId = this.currentId + this.blockSize;
		try {
			helper.writeProps("config/sequence.properties", "sequence.properties", super.sequenceName, Integer.toString(this.cacheId));
		} catch (IOException e) {
			
			log.error("writeProps error", e);
		}
	}
	
	

}

