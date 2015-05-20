/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.common;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ptnetwork.enterpriseSms.client.tools.PropertiesHelper;

/**
 * @author jasonzhang
 *
 */
public class DataSourceUtil {
	private static final Logger logger = Logger.getLogger(DataSourceUtil.class);

	public final static DataSource getDataSource() {
		ComboPooledDataSource dataSource = null;
		PropertiesHelper propsHelper = PropertiesHelper.getInstance();
		Properties props = propsHelper.loadProps("config/database.properties",
				"database.properties");
		if (props != null) {
			dataSource = new ComboPooledDataSource();
			try {
				dataSource.setDriverClass(props.getProperty("driverClass"));
			} catch (PropertyVetoException e) {
				logger.error(e.getMessage(), e);
			}
			dataSource.setUser(props.getProperty("user"));
			dataSource.setPassword(props.getProperty("password"));
			dataSource.setMinPoolSize(Integer.parseInt(props
					.getProperty("minPoolSize")));
			dataSource.setMaxPoolSize(Integer.parseInt(props
					.getProperty("maxPoolSize")));
			dataSource.setJdbcUrl(props.getProperty("jdbcUrl"));
			dataSource.setAutoCommitOnClose(false);
		} else {
			logger.error("database.properties is not exists!");
		}
		return dataSource;
	}

	public final static void destoryDataSource(DataSource dataSource) {
		((ComboPooledDataSource) dataSource).close();
	}
}
