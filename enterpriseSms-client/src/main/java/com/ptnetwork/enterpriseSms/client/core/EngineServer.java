/**
 * Project Name:smPlatform
 * File Name:EngineServer.java
 * Package Name:com.ptnetwork.enterpriseSms.client.core
 * Date:2012-11-28����10:03:57
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ptnetwork.enterpriseSms.client.core;

import java.beans.PropertyVetoException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ptnetwork.enterpriseSms.client.common.FileSequence;
import com.ptnetwork.enterpriseSms.client.domain.Login;
import com.ptnetwork.enterpriseSms.client.net.SmsMOClient;
import com.ptnetwork.enterpriseSms.client.net.SmsMTClient;
import com.ptnetwork.enterpriseSms.client.persistence.DbDeliverStore;
import com.ptnetwork.enterpriseSms.client.persistence.DbSubmitStore;
import com.ptnetwork.enterpriseSms.client.tools.PropertiesHelper;

/**
 * ClassName:EngineServer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2012-11-28 ����10:03:57 <br/>
 * @author   JasonZhang
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class EngineServer {
	private static Log log = LogFactory.getLog(EngineServer.class);
	private MOSP moSp = null;
	private MTSP mtSp = null;
	private ComboPooledDataSource dataSource = null;
	private DbSubmitStore submitStore = null;
	private DbDeliverStore deliverStore = null;

	public void start() {
		PropertiesHelper propsHelper = PropertiesHelper.getInstance();
		this.initDataSource(propsHelper);
		this.initSubmitStore();
		this.initDeliverStore();
		this.initSp(propsHelper);
		if (this.moSp != null) {
			//TODO �Ƿ�Ҫ�����¼���쳣���?
			this.moSp.login();
		}
		if (this.mtSp != null) {
			this.mtSp.login();
		}
	}
	
	private void initSp(PropertiesHelper propsHelper) {
		//�������ļ��ж�ȡsp�ĳ�ʼ������
		Properties props = propsHelper.loadProps("config/sp.properties", "sp.properties");
		if (props != null) {
			moSp = new MOSP();
			Login moLogin = new Login();
			moLogin.setIp(props.getProperty("serverIp"));
			moLogin.setPort(Integer.parseInt(props.getProperty("serverPort")));
			moLogin.setUserName(props.getProperty("name"));
			moLogin.setPassword(props.getProperty("password"));
			moLogin.setLoginMode(0);//发短信模式
			moSp.setLogin(moLogin);
			//set smsclient
			SmsMOClient moClient = new SmsMOClient();
			moSp.setSmsClient(moClient);
			FileSequence submitSequence = new FileSequence();
			submitSequence.setSequenceName("submit_sequence");
			moSp.setSubmitSequence(submitSequence);
			mtSp = new MTSP();
			Login mtLogin = new Login();
			mtLogin.setIp(props.getProperty("serverIp"));
			mtLogin.setPort(Integer.parseInt(props.getProperty("serverPort")));
			mtLogin.setUserName(props.getProperty("name"));
			mtLogin.setPassword(props.getProperty("password"));
			mtLogin.setLoginMode(1);//发短信模式
			mtSp.setLogin(mtLogin);
			//set smsclient
			SmsMTClient mtClient = new SmsMTClient();
			mtSp.setSmsClient(mtClient);
		} else {
			log.warn("sp properties is null app will exit");
			System.exit(1);
		}
		if (this.submitStore != null) {
			moSp.setSubmitStore(this.submitStore);
		}
		if (this.deliverStore != null){
			mtSp.setDeliverStore(deliverStore);
		}
	}
	
	private void initSubmitStore() {
		
		if (this.dataSource != null) {
			this.submitStore = new DbSubmitStore();
			submitStore.setDataSource(this.dataSource);
		}
	}
	
	private void initDeliverStore() {
		
		if (this.dataSource != null) {
			this.deliverStore = new DbDeliverStore();
			this.deliverStore.setDataSource(this.dataSource);
		}
	}
	
	private void initDataSource(PropertiesHelper propsHelper) {
		//�������ļ��ж�ȡ�����ʼ�����Դ
		Properties props = propsHelper.loadProps("config/database.properties", "database.properties");
		if (props != null) {
			this.dataSource = new ComboPooledDataSource();
			try {
				dataSource.setDriverClass(props.getProperty("driverClass"));
			} catch (PropertyVetoException e) {
				log.error(e.getMessage(), e);
			}
			dataSource.setUser(props.getProperty("user"));
			dataSource.setPassword(props.getProperty("password"));
			dataSource.setMinPoolSize(Integer.parseInt(props.getProperty("minPoolSize")));
			dataSource.setMaxPoolSize(Integer.parseInt(props.getProperty("maxPoolSize")));
			dataSource.setJdbcUrl(props.getProperty("jdbcUrl"));
			dataSource.setAutoCommitOnClose(false);
		} else {
			log.warn("database properties is null app will exit");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				log.error("thread sleep error", e);
			}
			System.exit(1);
		}
	}
	
	public void stop() {
		log.info("stop the engine server");
		if (this.moSp != null) {
			moSp.exit();
		} else {
			log.warn("sp is null!");
		}
		if (this.mtSp != null) {
			this.mtSp.exit();
		}
		if (this.dataSource != null) {
			this.dataSource.close();
		} else {
			log.warn("data source is null");
		}
	}
}

