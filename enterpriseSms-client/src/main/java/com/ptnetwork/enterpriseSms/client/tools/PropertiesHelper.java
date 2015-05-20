/**
 * Project Name:smPlatform
 * File Name:PropertiesHelper.java
 * Package Name:com.ptnetwork.dataCompress.tools
 * Date:2012-11-28����11:44:09
 * Copyright (c) 2012, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.client.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��?�����ļ������� ClassName:PropertiesHelper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2012-11-28 ����11:44:09 <br/>
 * 
 * @author JasonZhang
 * @version
 * @since JDK 1.6
 * @see
 */
public class PropertiesHelper {
	private static Log log = LogFactory.getLog(PropertiesHelper.class);
	
	private final static PropertiesHelper instance = new PropertiesHelper();
	
	private PropertiesHelper(){}
	
	public final static PropertiesHelper getInstance() {
		return instance;
	}

	/**
	 * 
	 * loadProps:(��ָ��·����?�����ļ�����). <br/>
	 * TODO(������������������� �C ��ѡ).<br/>
	 * TODO(��������������ִ����� �C ��ѡ).<br/>
	 * TODO(��������������ʹ�÷��� �C ��ѡ).<br/>
	 * TODO(��������������ע������ �C ��ѡ).<br/>
	 * 
	 * @author JasonZhang
	 * @param filePath
	 *            ���ò����ļ���·��
	 * @param defaultPath
	 *            ?���һ������е��ļ�����������������ṩ��·���ж�?�����ļ�
	 * @return
	 * @since JDK 1.6
	 */
	public Properties loadProps(String filePath, String defaultPath) {
		Properties props = null;
		InputStream inputStream = null;
		File file = null;

		try {
			file = configFile(filePath);
			inputStream = new FileInputStream(file);
		} catch (Exception e) {
			log.warn(filePath + " is not exist in config path!");
		}
		if (inputStream == null) {
			inputStream = getClass().getClassLoader().getResourceAsStream(
					defaultPath);
		}
		if (inputStream == null) {
			log.error(defaultPath + " is not exist!");
		} else {
			props = new Properties();
			try {
				props.load(inputStream);
			} catch (IOException e) {
				log.error("props load error", e);
			}
		}

		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error("inputStream close error", e);
			}
		}

		return props;
	}

	public void writeProps(String filePath, String defaultPath, String key, String value)
			throws IOException {
		File file = configFile(filePath);
		InputStream inputStream = null;
		try {
			file = configFile(filePath);
			inputStream = new FileInputStream(file);
		} catch (Exception e) {
			log.warn(filePath + " is not exist in config path!");
		}
		if (inputStream == null) {
			inputStream = getClass().getClassLoader().getResourceAsStream(
					defaultPath);
		}
		Properties props = new Properties();
		props.load(inputStream);
		props.setProperty(key, value);
		OutputStream outputStream = new FileOutputStream(file);
		props.store(outputStream, "update " + key + " " + value);
		inputStream.close();
		outputStream.close();
	}

	private File configFile(String filePath) {
		File file = new File(System.getProperty("catalina.home"), filePath);
		return file;

	}
}
