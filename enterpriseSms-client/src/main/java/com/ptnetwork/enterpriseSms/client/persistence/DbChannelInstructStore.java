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

import com.ptnetwork.enterpriseSms.client.domain.ChannelInstruct;

/**
 * @author jasonzhang
 *
 */
public class DbChannelInstructStore extends DbBaseStore {

	private static Log log = LogFactory.getLog(DbBillRequestStore.class);
	
	private final static String GET_CHANNEL_INSTRUCT = "select id, instruct, channel_id from sm_channel_instruct";
	private final static String INSERT_CHANNEL_INSTRUCT = "insert into sm_channel_instruct(id, instruct, channel_id) values(?,?,?)";
	private final static String UPDATE_CHANNEL_INSTRUCT_INFO = "update sm_channel_instruct set instruct = ? where id = ?";
	private final static String DELETE_CHANNEL_INSTRUCT = "delete from sm_channel_instruct where id = ?";
	
	public void saveChannelInstruct(ChannelInstruct channelInstruct) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(INSERT_CHANNEL_INSTRUCT);
			pstmt.setString(1, channelInstruct.getId());
			pstmt.setString(2, channelInstruct.getInstruct());
			pstmt.setString(3, channelInstruct.getChannelId());
			pstmt.executeUpdate();
			super.closePstmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			super.rollbackConnection(conn);
			log.error("exception when getChannelInstructList", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public void updatChannelInstructInfo(String id, String instruct) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(UPDATE_CHANNEL_INSTRUCT_INFO);
			pstmt.setString(1, instruct);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			super.closePstmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			super.rollbackConnection(conn);
			log.error("exception when updatChannelInstructInfo", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public void deleteChannelInstruct(String id) throws DaoException {
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(DELETE_CHANNEL_INSTRUCT);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			super.closePstmt(pstmt);
			conn.commit();
		} catch (SQLException e) {
			super.rollbackConnection(conn);
			log.error("exception when deleteChannelInstruct", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public List<ChannelInstruct> getChannelInstructList() throws DaoException {
		List<ChannelInstruct> list = new ArrayList<ChannelInstruct>();
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement(GET_CHANNEL_INSTRUCT);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ChannelInstruct channelInstruct = new ChannelInstruct();
				channelInstruct.setId(rs.getString(1));
				channelInstruct.setInstruct(rs.getString(2));
				channelInstruct.setChannelId(rs.getString(3));
				list.add(channelInstruct);
			}
			super.closeResultSet(rs);
			super.closeStmt(pstmt);
		} catch (SQLException e) {

			log.error("exception when getChannelInstructList", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
		return list;
	}
}
