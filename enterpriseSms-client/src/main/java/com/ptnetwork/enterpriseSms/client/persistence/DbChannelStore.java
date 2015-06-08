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

import com.ptnetwork.enterpriseSms.client.domain.Channel;

/**
 * @author jasonzhang
 *
 */
public class DbChannelStore extends DbBaseStore {

	private static Log log = LogFactory.getLog(DbChannelStore.class);
	
	private final static String INSERT_CHANNEL =  "insert into sm_channel(id, post_url) values(?, ?)";
	private final static String GET_CHANNEL = "select id, post_url from sm_channel";
	private final static String UPDATE_CHANNEL_POST_URL = "update sm_channel set post_url = ? where id = ?";
	
	public void saveChannel(Channel channel) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(INSERT_CHANNEL);
			pstmt.setString(1, channel.getId());
			pstmt.setString(2, channel.getPostUrl());
			pstmt.executeUpdate();
			super.closePstmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			super.rollbackConnection(conn);
			log.error("exception when saveChannel", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public void updatePostUrl(String id, String postUrl) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(UPDATE_CHANNEL_POST_URL);
			pstmt.setString(1, postUrl);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			super.closePstmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			super.rollbackConnection(conn);
			log.error("exception when updatePostUrl", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public List<Channel> getChannelList() throws DaoException {
		List<Channel> list = new ArrayList<Channel>();
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(GET_CHANNEL);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Channel channel = new Channel();
				channel.setId(rs.getString(1));
				channel.setPostUrl(rs.getString(2));
				list.add(channel);
			}
			super.closeResultSet(rs);
			super.closeStmt(pstmt);
		} catch (SQLException e) {

			log.error("exception when getChannelList", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
		return list;
	}
}
