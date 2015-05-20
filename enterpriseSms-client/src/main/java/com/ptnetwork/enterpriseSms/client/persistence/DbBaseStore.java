/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * @author jasonzhang
 *
 */
public abstract class DbBaseStore {

	private static final Logger logger = Logger.getLogger(DbBaseStore.class);
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected Connection getConnection() throws SQLException {
		Connection conn = this.dataSource.getConnection();
		conn.setAutoCommit(false);
		return conn;
	}
	
	protected void closePstmt(PreparedStatement pstmt) throws DaoException {
		if (pstmt != null) {
			try {
//				if (!pstmt.isClosed()) {
					pstmt.close();
//				}
				
			} catch (SQLException e) {
				logger.error("exception when closePstmt", e);
				throw new DaoException(e.getMessage());
			}
		}
	}
	
	protected void closeStmt(Statement stmt) throws DaoException {
		if (stmt != null) {
			try {
//				if (!stmt.isClosed()) {
					stmt.close();
//				}
			} catch (SQLException e) {
				logger.error("exception when closePstmt", e);
				throw new DaoException(e.getMessage());
			}
		}
	}
	
	protected void closeConnction(Connection conn) throws DaoException {
		if (conn != null) {
			try {
//				if (!conn.isClosed()) {
					conn.close();
//				}
			} catch (SQLException e) {
				logger.error("exception when closeConnction", e);
				throw new DaoException(e.getMessage());
			}
		}
	}
	
	protected void closeResultSet(ResultSet rs) throws DaoException {
		if (rs != null) {
			try {
//				if (!rs.isClosed()) {
					rs.close();
//				}
			} catch (SQLException e) {
				logger.error("exception when closeResultSet", e);
				throw new DaoException(e.getMessage());
			}
		}
	}
	
	protected void rollbackConnection(Connection conn) throws DaoException {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error("exception when rollbackConnection", e);
				throw new DaoException(e.getMessage());
			}
		}
	}
	
	protected Timestamp convertSqlDate(java.util.Date date) {
		return (date != null ? new Timestamp(date.getTime()) : null);
	}
}
