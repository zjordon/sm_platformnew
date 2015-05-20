/**
 * Project Name:smPlatform
 * File Name:DbDeliverStore.java
 * Package Name:com.ptnetwork.enterpriseSms.client.persistence
 * Date:2013-1-12上午11:20:50
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
 */
/**
 * Project Name:smPlatform
 * File Name:DbDeliverStore.java
 * Package Name:com.ptnetwork.enterpriseSms.client.persistence
 * Date:2013-1-12上午11:20:50
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.ptnetwork.enterpriseSms.client.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.domain.Deliver;

/**
 * ClassName:DbDeliverStore <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2013-1-12 上午11:20:50 <br/>
 * 
 * @author JasonZhang
 * @version
 * @since JDK 1.6
 * @see
 */
public class DbDeliverStore extends DbBaseStore {
	
	private static Log log = LogFactory.getLog(DbDeliverStore.class);

	private static String INSERT_DELIVER = "insert into sm_deliver(id, msg_id, dest_term_id, service_id, msg_format, src_term_id, is_report, msg_length, msg_content, create_date)"
			+ "values(?,?,?,?,?,?,?,?,?,?)";

	public void saveDeliver(Deliver deliver) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(INSERT_DELIVER);
			this.setParameter(pstmt, deliver);
			pstmt.executeUpdate();
			super.closeStmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			log.error("insert deliver exception", e);
			throw new DaoException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
	}
	
	public void saveDeliverList(List<Deliver> deliverList) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(INSERT_DELIVER);
			for (Deliver deliver : deliverList) {
				this.setParameter(pstmt, deliver);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			super.closeStmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			log.error("insert deliver exception", e);
			throw new DaoException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
	}
	
	private void setParameter(PreparedStatement pstmt, Deliver deliver) throws SQLException {
		pstmt.setString(1, deliver.getId());
		pstmt.setLong(2, deliver.getMsgId());
		pstmt.setString(3, deliver.getDestTermId());
		pstmt.setString(4, deliver.getServiceId());
		pstmt.setInt(5, deliver.getMsgFormat());
		pstmt.setString(6, deliver.getSrcTermId());
		pstmt.setBoolean(7, deliver.isReport());
		pstmt.setInt(8, deliver.getMsgLength());
		pstmt.setString(9, deliver.getMsgContent());
		pstmt.setTimestamp(10, super.convertSqlDate(deliver.getCreateDate()));
	}
}
