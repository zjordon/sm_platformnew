package com.ptnetwork.enterpriseSms.client.persistence;

import javax.sql.DataSource;

import com.ptnetwork.enterpriseSms.client.common.DataSourceUtil;

import junit.framework.TestCase;

public class BaseStoreTest extends TestCase {
	protected DataSource dataSource;

	protected void setUp() throws Exception {
		super.setUp();
		this.dataSource = DataSourceUtil.getDataSource();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
