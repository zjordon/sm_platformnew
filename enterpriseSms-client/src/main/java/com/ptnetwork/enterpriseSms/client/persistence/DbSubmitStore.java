/**
 * @(#) DbSubmitStore.java 2006-3-2
 * 
 * Copyright 2006 ptnetwork
 */
package com.ptnetwork.enterpriseSms.client.persistence;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.domain.Submit;

/**
 * @author jasonzhang
 *
 */
public class DbSubmitStore {
	protected static Log log = LogFactory.getLog(DbSubmitStore.class);
	private DataSource dataSource;
	public DataSource getDataSource() {
		if (dataSource == null) {
			//TODO 
		}
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private int[] capacityLimits = new int[9];
	private int[] messageRecords = new int[9];
	private static String LOAD_CAPACITYLIMITS = 
		"select id, limitnums from capacitylimit order by id asc";
	public void initCapLimits() {
		//�����ñ��ж�ȡ�����е������Ƽ�¼
		Connection conn = null;
		try {
			conn = this.getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(LOAD_CAPACITYLIMITS);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				capacityLimits[rs.getInt(1) - 1] = rs.getInt(2);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void clearMessageRecords() {
		//�Ѹ����е�����¼��Ϊ��
		synchronized(messageRecords) {
			for (int i=0; i<9; i++) {
				messageRecords[i] = 0;
			}
		}
	}

	public DbSubmitStore() {
		
	}
	
	private static String INSERT_SUBMIT = 
		"insert into client_submit(msgtype, needreport, priority, serviceid, feetype, " +
		"fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount," +
		"desttermid, msglength, msgcontent, state, instructionid, trafficid, id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
		"?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public void addSubmit(Submit msg) {
		//�ж���Ϣ�����Ƿ񳬳�(����212���ֽ�)
		byte[] bs = null;
		try {
			bs = msg.getMsgContent().getBytes("GBK");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (bs.length > 212) {
			this.addBatchSubmit(msg, bs);
			return;
		}
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(INSERT_SUBMIT);
			//pstmt.setLong(1, msg.getId());
			pstmt.setInt(1, msg.getMsgType());
			pstmt.setInt(2, msg.getNeedReport());
			pstmt.setInt(3, msg.getPriority());
			pstmt.setString(4, msg.getServiceId());
			pstmt.setString(5, msg.getFeeType());
			pstmt.setString(6, msg.getFixedFee());
			pstmt.setString(7, msg.getFeeCode());
			pstmt.setString(8, msg.getValidTime());
			pstmt.setString(9, msg.getAtTime());
			pstmt.setString(10, msg.getSrcTermId());
			pstmt.setString(11, msg.getChargeTermId());
			pstmt.setInt(12, msg.getDestTermIdCount());
			pstmt.setString(13, msg.getDestTermId());
			pstmt.setInt(14, msg.getMsgLength());
			pstmt.setString(15, msg.getMsgContent());
			pstmt.setInt(16, msg.getState());
			pstmt.setInt(17, msg.getInstructionId());
			pstmt.setInt(18, msg.getTrafficId());
			pstmt.setString(19, UUID.randomUUID().toString());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
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
	
	private void addBatchSubmit(Submit msg, byte[] bs) {
		String content = msg.getMsgContent();
		int size = content.length()/106 + 1;
//		int size = bs.length / 212 + 1;
		String[] msgContents = new String[size];
		for (int i=0; i<size; i++) {
//			int len = (i + 1) * 212 < bs.length ? 212 : (bs.length - i * 212);
			int len = (i + 1) * 106 < content.length() ? 106 : (content.length() - i * 106);
			msgContents[i] = content.substring(i * 106, i * 106 + len);
//			try {
//				msgContents[i] = new String(bs, i * 212, len, "GBK");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
		}
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(INSERT_SUBMIT);
			for (int i=0; i<msgContents.length; i++) {
				String msgContent = msgContents[i];
				pstmt.setInt(1, msg.getMsgType());
				pstmt.setInt(2, msg.getNeedReport());
				pstmt.setInt(3, msg.getPriority());
				pstmt.setString(4, msg.getServiceId());
				pstmt.setString(5, msg.getFeeType());
				pstmt.setString(6, msg.getFixedFee());
				pstmt.setString(7, msg.getFeeCode());
				pstmt.setString(8, msg.getValidTime());
				pstmt.setString(9, msg.getAtTime());
				pstmt.setString(10, msg.getSrcTermId());
				pstmt.setString(11, msg.getChargeTermId());
				pstmt.setInt(12, msg.getDestTermIdCount());
				pstmt.setString(13, msg.getDestTermId());
				pstmt.setInt(14, msg.getMsgLength());
				pstmt.setString(15, msgContent);
				pstmt.setInt(16, msg.getState());
				pstmt.setInt(17, msg.getInstructionId());
				pstmt.setInt(18, msg.getTrafficId());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
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
	
	private static String INSERT_SUBMIT_FAIL =
		"insert into client_submit_fail(id, msgtype, needreport, priority, serviceid, feetype, " +
		"fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount," +
		"desttermid, msglength, msgcontent, msgid, timewindowid, instructionid, trafficid) " +
		"(select id, msgtype, needreport, priority, serviceid, feetype, " +
		"fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount," +
		"desttermid, msglength, msgcontent, msgid, timewindowid, instructionid, trafficid from client_submit where priority = 0)";
	private static String DELETE_SUBMIT_FAIL = 
		"delete from client_submit where priority = 0";
	public void deleteOldSubmit() {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(INSERT_SUBMIT_FAIL);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = conn.prepareStatement(DELETE_SUBMIT_FAIL);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
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

	private static String INSERT_SUBMIT_SUCCESS = 
		"insert into client_submit_success(id, msgtype, needreport, priority, serviceid, feetype, " +
		"fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount," +
		"desttermid, msglength, msgcontent, msgid, timewindowid, instructionid, trafficid) " +
		"(select id, msgtype, needreport, priority, serviceid, feetype, " +
		"fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount," +
		"desttermid, msglength, msgcontent, msgid, timewindowid, instructionid, trafficid from client_submit where id = ?)";
	private static String DELETE_SUBMIT = "delete from client_submit where id = ?";
	public void deleteSubmit(String id) {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			this.deleteSubmitIntra(conn, id);
		} catch (SQLException e) {
			log.error(e);
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
	
	private void deleteBatchSubmitIntra(Connection conn, long[] ids) throws SQLException {
		StringBuffer builder = new StringBuffer();
		builder.append("insert into client_submit_success(id, msgtype, needreport, priority, serviceid, feetype, ");
		builder.append("fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount,");
		builder.append("desttermid, msglength, msgcontent, msgid, timewindowid, instructionid, trafficid) ");
		builder.append("(select id, msgtype, needreport, priority, serviceid, feetype, ");
		builder.append("fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount,");
		builder.append("desttermid, msglength, msgcontent, msgid, timewindowid, instructionid, trafficid from client_submit where id in(");
		for (int i=0; i<ids.length; i++) {
			long id  = ids[i];
			builder.append(id).append(',');
		}
		builder.setCharAt(builder.length() - 1, ')');
		builder.append(')');
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(builder.toString());
		builder = new StringBuffer();
		builder.append("delete from client_submit where id in(");
		for (int i=0; i<ids.length; i++) {
			long id  = ids[i];
			builder.append(id).append(',');
		}
		builder.setCharAt(builder.length() - 1, ')');
		stmt.executeUpdate(builder.toString());
		stmt.close();
	}
	
	private void deleteSubmitIntra(Connection conn, String id) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(INSERT_SUBMIT_SUCCESS);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		pstmt.close();
		pstmt = null;
		pstmt = conn.prepareStatement(DELETE_SUBMIT);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		pstmt.close();
		pstmt = null;
	}

	//oracle
//	private static String SELECT_SUBMITS = 
//		"select id, msgtype, needreport, priority, serviceid, feetype," +
//		"fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount," +
//		"desttermid, msglength, msgcontent, state, TRAFFICID from " +
//		"(select id, msgtype, needreport, priority, serviceid, feetype," +
//		"fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount," +
//		"desttermid, msglength, msgcontent, state, TRAFFICID from submit where state = ? and " +
//		"(timewindowid is null or timewindowid = 0) order by priority desc, id asc) where rownum <= ?";
	//mysql
	private static String SELECT_SUBMITS = 
		"select id, msgtype, needreport, priority, serviceid, feetype," +
		"fixedfee, feecode, validtime, attime, srctermid, chargetermid, desttermidcount," +
		"desttermid, msglength, msgcontent, state, TRAFFICID,msgid from client_submit where state = ? " +
		" order by priority desc, id asc limit 0, ?";
	public List<Submit> getSubmits(int nums,int state,boolean update) {
		List<Submit> retList = new ArrayList<Submit>(nums);
		loadSubmits(state, nums, SELECT_SUBMITS, retList, update);
		return retList;
	}
	
	/*
	 *  ��ȡ��һ��ֹͣ����ʱû�з��͵�������Ϣ��û��ʱ�䴰�����ƣ�
	 *  �����ڷ��͵���Ϣ״̬��Ϊ0
	 */
	private static String UPDATE_SENDING = 
		"update client_submit set state = 0 where state = 1";
	public List<Submit> getSendings(int nums) {
		List<Submit> retList = new ArrayList<Submit>(nums);
//		loadSubmits(1, nums, SELECT_SUBMITS, retList, false);
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(UPDATE_SENDING);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
		return retList;
	}
	
	private void loadSubmits(int state, int nums, String sql, List<Submit> retList, boolean update) {
		Connection conn = null;
		StringBuffer buffer = new StringBuffer();
		buffer.append("update client_submit set state = 1 where id in(");
		long currentTime = System.currentTimeMillis();
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, state);
			pstmt.setInt(2, nums);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Submit submit = new Submit();
				submit.setSendTime(currentTime);
				String id = rs.getString(1);
				submit.setId(id);
				submit.setMsgType(rs.getInt(2));
				submit.setNeedReport(rs.getInt(3));
				submit.setPriority(rs.getInt(4));
				submit.setServiceId(rs.getString(5));
				submit.setFeeType(rs.getString(6));
				submit.setFixedFee(rs.getString(7));
				submit.setFeeCode(rs.getString(8));
				submit.setValidTime(rs.getString(9));
				submit.setAtTime(rs.getString(10));
				submit.setSrcTermId(rs.getString(11));
				submit.setChargeTermId(rs.getString(12));
				submit.setDestTermIdCount(rs.getInt(13));
				submit.setDestTermId(rs.getString(14));
				submit.setMsgLength(rs.getInt(15));
				//for mysql
//				try {
//					submit.setMsgContent(new String(rs.getString(16).getBytes("ISO8859-1"), "GBK"));
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//					log.error(e);
//				}
				submit.setMsgContent(rs.getString(16));
				submit.setState(rs.getInt(17));
				submit.setTrafficId(rs.getInt(18));
				submit.setMsgId(rs.getString(19));
//				if (submit.getPriority() == 0) {
//					//���ȼ��ϵ͵Ķ���һ�����Ⱥ�����ţ�������������
//					int index = ((Character.digit(submit.getDestTermId().charAt(3), 10)) - 1);
//					if (this.messageRecords[index] < this.capacityLimits[index]) {
//						this.incrementRecord(index);
//						retList.add(submit);
//						buffer.append(id).append(',');
//					} else {
//						System.out.println("messageRecords index is " + this.messageRecords[index] + " capacityLimits is " + this.capacityLimits[index]);
//						System.out.println("capacity limit with id " + submit.getId() + " destTemrId is " + submit.getDestTermId());
//					}
//				} else {
//					retList.add(submit);
//					buffer.append(id).append(',');
//				}
				retList.add(submit);
				buffer.append('\'').append(id).append("',");
				
			}
			rs.close();
			pstmt.close();
			if(update && !retList.isEmpty()) {
				buffer.setCharAt(buffer.length() -1, ')');
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(buffer.toString());
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			log.error("loadSubmits exception", e);
			retList.clear();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error("rollback error", e1);
			}
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

	private static String UPDATE_SUBMIT = 
		"update client_submit set msgtype = ?, needreport = ?, priority = ?, serviceid = ?, " +
		"feetype = ?, fixedfee = ?, feecode = ?, validtime = ?, attime = ?, " +
		"srctermid = ?, chargetermid = ?, desttermidcount = ?, " +
		"desttermid = ?, msglength = ?, msgcontent = ?, state = ? where id = ?";
	public void updateSubmit(Submit msg) {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(UPDATE_SUBMIT);
			pstmt.setInt(1, msg.getMsgType());
			pstmt.setInt(2, msg.getNeedReport());
			pstmt.setInt(3, msg.getPriority());
			pstmt.setString(4, msg.getServiceId());
			pstmt.setString(5, msg.getFeeType());
			pstmt.setString(6, msg.getFixedFee());
			pstmt.setString(7, msg.getFeeCode());
			pstmt.setString(8, msg.getValidTime());
			pstmt.setString(9, msg.getAtTime());
			pstmt.setString(10, msg.getSrcTermId());
			pstmt.setString(11, msg.getChargeTermId());
			pstmt.setInt(12, msg.getDestTermIdCount());
			pstmt.setString(13, msg.getDestTermId());
			pstmt.setInt(14, msg.getMsgLength());
			pstmt.setString(15, msg.getMsgContent());
			pstmt.setInt(16, msg.getState());
			pstmt.setString(17, msg.getId());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
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
	
	private static String UPDATE_SUBMIT_STATE = "update client_submit set state = ?, msgid = ? where id = ?";
	public void updateSubmitState(String id, int state, String msgId) {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(UPDATE_SUBMIT_STATE);
			pstmt.setInt(1, state);
			pstmt.setString(2, msgId);
			pstmt.setString(3, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
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
	
	private static String UPDATE_SUBMIT_STATE_SEQUENCE = "update client_submit set state = ?, sequence_id = ? where id = ?";
	public void updateSubmitState(String id, int state, int sequenceId) {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(UPDATE_SUBMIT_STATE_SEQUENCE);
			pstmt.setInt(1, state);
			pstmt.setInt(2, sequenceId);
			pstmt.setString(3, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
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

	private static String UPDATE_MSGID = 
		"update client_submit set msgid=? where id=?";
	public void updateSubmitMsgId(String id, String msgId) {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			this.updateMsgIdIntra(conn, id, msgId);
		} catch (SQLException e) {
			log.error(e);
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
	
	private void updateMsgIdIntra(Connection conn, String id, String msgId) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(UPDATE_MSGID);
		pstmt.setString(1, msgId);
		pstmt.setString(2, id);
		pstmt.executeUpdate();
		pstmt.close();
		pstmt = null;
	}
	
	public void updateMsgIdAndDel(String id, String msgId) {
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			this.deleteSubmitIntra(conn, id);
			this.updateMsgIdIntra(conn, id, msgId);
		} catch (SQLException e) {
			log.error(e);
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
	
	public void updateBatchMsgIdAndDel(long[] ids, String[] msgIds) {
		if (ids == null || ids.length == 0) {
			return;
		}
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			this.deleteBatchSubmitIntra(conn, ids);
			PreparedStatement pstmt = conn.prepareStatement(UPDATE_MSGID);
			for (int i=0; i<ids.length; i++) {
				pstmt.setString(1, msgIds[i]);
				pstmt.setLong(2, ids[i]);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
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
	
	public void deleteBatchSubmit(long[] ids) {
		Connection conn = null;
		try {
			this.deleteBatchSubmitIntra(conn, ids);
		} catch (SQLException e) {
			log.error(e);
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
	
	private static String GET_SUBMIT_SUCCESS_BY_MSGID = 
		"select desttermid, msgcontent, instructionid, trafficid, id from client_submit_success where msgid=?";
	public Submit getSubmitSuccessByMsgId(String msgId) {
		Submit submit = null;
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(GET_SUBMIT_SUCCESS_BY_MSGID);
			pstmt.setString(1, msgId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				submit = new Submit();
				submit.setDestTermId(rs.getString(1));
				submit.setMsgContent(rs.getString(2));
				submit.setInstructionId(rs.getInt(3));
				submit.setTrafficId(rs.getInt(4));
				submit.setId(rs.getString(5));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.error(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
		return submit;
	}

}
