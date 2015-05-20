/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.domain.ChannelUser;

/**
 * @author jasonzhang
 *
 */
public class DbChannelUserStore extends DbBaseStore {

	private static Log log = LogFactory.getLog(DbBillRequestStore.class);
	
	private final static String GET_CHANNEL_USER = "select id, username, password, channel_id from sm_channel_user";
	private final static String INSERT_CHANNEL_USER = "insert into sm_channel_user(id, username, password, channel_id) values(?,?,?,?)";
	private final static String UPDATE_CHANNEL_USER_INFO = "update sm_channel_user set password = ? where id = ?";
	private final static String DELETE_CHANNEL_USER = "delete from sm_channel_user where id = ?";
	
	public void saveChannelUser(ChannelUser channelUser) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(INSERT_CHANNEL_USER);
			pstmt.setString(1, channelUser.getId());
			pstmt.setString(2, channelUser.getUsername());
			pstmt.setString(3, channelUser.getPassword());
			pstmt.setString(4, channelUser.getChannelId());
			pstmt.executeUpdate();
			super.closePstmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			super.rollbackConnection(conn);
			log.error("exception when getChannelUserList", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public void updatChannelUserInfo(String id, String password) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(UPDATE_CHANNEL_USER_INFO);
			pstmt.setString(1, password);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			super.closePstmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			super.rollbackConnection(conn);
			log.error("exception when updatChannelUserInfo", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public void deleteChannelUser(String id) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(DELETE_CHANNEL_USER);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			super.closePstmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			super.rollbackConnection(conn);
			log.error("exception when deleteChannelUser", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public List<ChannelUser> getChannelUserList() throws DaoException {
		List<ChannelUser> list = new ArrayList<ChannelUser>();
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(GET_CHANNEL_USER);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ChannelUser channelUser = new ChannelUser();
				channelUser.setId(rs.getString(1));
				channelUser.setUsername(rs.getString(2));
				channelUser.setPassword(rs.getString(3));
				channelUser.setChannelId(rs.getString(4));
				list.add(channelUser);
			}
			super.closeResultSet(rs);
			super.closeStmt(pstmt);
		} catch (SQLException e) {

			log.error("exception when getChannelUserList", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
		return list;
	}
}
