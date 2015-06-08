/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ptnetwork.enterpriseSms.client.domain.BillRequest;
import com.ptnetwork.enterpriseSms.client.domain.RepestRequestRecord;
import com.ptnetwork.enterpriseSms.client.common.UUIDGenerator;

/**
 * @author jasonzhang
 *
 */
public class DbBillRequestStore extends DbBaseStore {

	private static Log log = LogFactory.getLog(DbBillRequestStore.class);
	
	private static String INSERT_BILL_REQUEST = "insert into sm_bill_request(id, user_name, user_pass, instruct, msisdn, repeat_flag, state, "
			+ "start_time, end_time, response_state, create_date, deliver_id, post_url)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private final static String UPDATE_BILL_REQUEST = "update sm_bill_request set start_time = ?, end_time = ?, response_state = ?, repeat_flag = ? where id = ?";
	private static String INSERT_REPEAT_RECORD = "insert into sm_repeat_request_record(id, start_time, end_time, response_state, create_date, bill_request_id)"
			+ "values(?,?,?,?,?,?)";
	private final static String GET_NEED_REPEAT_REQUET = "select id, user_name, user_pass, instruct, msisdn, repeat_flag, state, "
			+ "start_time, end_time, response_state, create_date, post_url from sm_bill_request where repeat_flag = 1 limit 0, ?";
	public void saveBillRequestList(List<BillRequest> billRequestList) throws DaoException {
		Connection conn = null;
		List<RepestRequestRecord> repeatRequestList = new ArrayList<RepestRequestRecord>();
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(INSERT_BILL_REQUEST);
			StringBuilder builder = new StringBuilder();
			for (BillRequest billRequest : billRequestList) {
				
				//如果需要重发，则把当条记录保存到历史记录
				if (billRequest.isRepeatFlag()) {
					this.setParameterToPstmt(pstmt, billRequest);
					pstmt.addBatch();
					this.addRepeatRecordToList(repeatRequestList, billRequest);
				} else {
					//如果不需要重发则只记录日志
					builder.append(billRequest.toString());
				}
			}
			pstmt.executeBatch();
			super.closeStmt(pstmt);
			if (!repeatRequestList.isEmpty()) {
				this.saveRepeatRecordList(conn, pstmt, repeatRequestList);
			}
			conn.commit();
			if (builder.length() > 0) {
				log.info(builder.toString());
			}
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
	
	public void updateBillRequestList(List<BillRequest> billRequestList) throws DaoException {
		Connection conn = null;
		List<RepestRequestRecord> repeatRequestList = new ArrayList<RepestRequestRecord>();
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(UPDATE_BILL_REQUEST);
			for (BillRequest billRequest : billRequestList) {
				pstmt.setTimestamp(1, super.convertSqlDate(billRequest.getStartTime()));
				pstmt.setTimestamp(2, super.convertSqlDate(billRequest.getEndTime()));
				pstmt.setString(3, billRequest.getResponseState());
				pstmt.setBoolean(4, billRequest.isRepeatFlag());
				pstmt.setString(5, billRequest.getId());
				pstmt.addBatch();
				//如果需要重发，则把当条记录保存到历史记录
				if (billRequest.isRepeatFlag()) {
					this.addRepeatRecordToList(repeatRequestList, billRequest);
				}
			}
			pstmt.executeBatch();
			super.closeStmt(pstmt);
			if (!repeatRequestList.isEmpty()) {
				this.saveRepeatRecordList(conn, pstmt, repeatRequestList);
			}
			conn.commit();
			
		} catch (SQLException e) {
			log.error("insert deliver exception", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
	}
	
	public List<BillRequest> getNeedRepeatRequestList(int num) throws DaoException {
		List<BillRequest> billRequestList = new ArrayList<BillRequest>(num);
		Connection conn = null;
		try {
			conn = super.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(GET_NEED_REPEAT_REQUET);
			pstmt.setInt(1, num);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BillRequest billRequest = new BillRequest();
				billRequest.setId(rs.getString(1));
				billRequest.setUserName(rs.getString(2));
				billRequest.setUserPass(rs.getString(3));
				billRequest.setInstruct(rs.getString(4));
				billRequest.setMsisdn(rs.getLong(5));
				billRequest.setRepeatFlag(rs.getBoolean(6));
				billRequest.setState(rs.getInt(7));
				billRequest.setStartTime(rs.getDate(8));
				billRequest.setEndTime(rs.getDate(9));
				billRequest.setResponseState(rs.getString(10));
				billRequest.setCreateDate(rs.getDate(11));
				billRequest.setPostUrl(rs.getString(12));
				billRequestList.add(billRequest);
			}
			super.closeResultSet(rs);
			super.closePstmt(pstmt);
		} catch (SQLException e) {
			log.error("insert deliver exception", e);
			throw new DaoException(e.getMessage());
		} finally {
			super.closeConnction(conn);
		}
		return billRequestList;
	}
	
	private void saveRepeatRecordList(Connection conn, PreparedStatement pstmt,List<RepestRequestRecord> repeatRequestList) throws SQLException {
		pstmt = conn.prepareStatement(INSERT_REPEAT_RECORD);
		for (RepestRequestRecord repeatRecord : repeatRequestList) {
			pstmt.setString(1, repeatRecord.getId());
			pstmt.setTimestamp(2, super.convertSqlDate(repeatRecord.getStartTime()));
			pstmt.setTimestamp(3, super.convertSqlDate(repeatRecord.getEndTime()));
			pstmt.setString(4, repeatRecord.getResponseState());
			pstmt.setTimestamp(5, super.convertSqlDate(repeatRecord.getCreateDate()));
			pstmt.setString(6, repeatRecord.getBillRequestId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();
	}
	
	private void addRepeatRecordToList(List<RepestRequestRecord> repeatRequestList, BillRequest billRequest) {
		RepestRequestRecord repestRequestRecord = new RepestRequestRecord();
		repestRequestRecord.setId((new UUIDGenerator()).generate());
		repestRequestRecord.setBillRequestId(billRequest.getId());
		repestRequestRecord.setCreateDate(new Date());
		repestRequestRecord.setResponseState(billRequest.getResponseState());
		repestRequestRecord.setStartTime(billRequest.getStartTime());
		repestRequestRecord.setEndTime(billRequest.getEndTime());
		repeatRequestList.add(repestRequestRecord);
	}
	
	private void setParameterToPstmt(PreparedStatement pstmt, BillRequest billRequest) throws SQLException {
		pstmt.setString(1, billRequest.getId());
		pstmt.setString(2, billRequest.getUserName());
		pstmt.setString(3, billRequest.getUserPass());
		pstmt.setString(4, billRequest.getInstruct());
		pstmt.setLong(5, billRequest.getMsisdn());
		pstmt.setBoolean(6, billRequest.isRepeatFlag());
		pstmt.setInt(7, billRequest.getState());
		pstmt.setTimestamp(8, super.convertSqlDate(billRequest.getStartTime()));
		pstmt.setTimestamp(9, super.convertSqlDate(billRequest.getEndTime()));
		pstmt.setString(10, billRequest.getResponseState());
		pstmt.setTimestamp(11, super.convertSqlDate(billRequest.getCreateDate()));
		pstmt.setString(12, billRequest.getDeliverId());
		pstmt.setString(13, billRequest.getPostUrl());
	}
}
