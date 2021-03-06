/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ptnetwork.enterpriseSms.client.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Utility class to read the bootstrap Catalina configuration.
 * 
 * @author Remy Maucherat
 * @author jasonzhang
 * @version $Revision: 1.1.1.1 $ $Date: 2006/04/25 09:29:00 $
 * 
 */
public class CatalinaProperties {

	private static Properties properties = null;

	static {

		loadProperties();

	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Return specified property value.
	 */
	public static String getProperty(String name) {

		return properties.getProperty(name);

	}

	/**
	 * Return specified property value.
	 */
	public static String getProperty(String name, String defaultValue) {

		return properties.getProperty(name, defaultValue);

	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Load properties.
	 */
	private static void loadProperties() {

		InputStream is = null;
		Throwable error = null;

		try {
			String configUrl = getConfigUrl();
			if (configUrl != null) {
				is = (new URL(configUrl)).openStream();
			}
		} catch (Throwable t) {
			// Ignore
		}

		if (is == null) {
			try {
				File home = new File(getCatalinaBase());
				File conf = new File(home, "config");
				File properties = new File(conf, "catalina.properties");
				is = new FileInputStream(properties);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		if (is == null) {
			try {
				is = CatalinaProperties.class
						.getResourceAsStream("/com/ptnetwork/enterpriseSms/client/startup/catalina.properties");
			} catch (Throwable t) {
				// Ignore
			}
		}

		if (is != null) {
			try {
				properties = new Properties();
				properties.load(is);
				is.close();
			} catch (Throwable t) {
				error = t;
			}
		}

		if ((is == null) || (error != null)) {
			// Do something
			// log.warn("Failed to load catalina.properties", error);
		}

		// Register the properties as system properties
		Enumeration enumeration = properties.propertyNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			String value = properties.getProperty(name);
			if (value != null) {
				System.setProperty(name, value);
			}
		}

	}

	/**
	 * Get the value of the catalina.home environment variable.
	 */
	private static String getCatalinaHome() {
		return System.getProperty("catalina.home", System
				.getProperty("user.dir"));
	}

	/**
	 * Get the value of the catalina.base environment variable.
	 */
	private static String getCatalinaBase() {
		return System.getProperty("catalina.base", getCatalinaHome());
	}

	/**
	 * Get the value of the configuration URL.
	 */
	private static String getConfigUrl() {
		return System.getProperty("catalina.config");
	}
}
