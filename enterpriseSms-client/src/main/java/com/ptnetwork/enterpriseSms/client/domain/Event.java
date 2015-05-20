package com.ptnetwork.enterpriseSms.client.domain;

/**   
 * @Title: Entity
 * @Description: 事件
 * @author zhangdaihao
 * @date 2015-03-30 09:09:34
 * @version V1.0   
 *
 */
@SuppressWarnings("serial")
public class Event implements java.io.Serializable {
	/**唯一标识*/
	private java.lang.String id;
	/**事件ID*/
	private java.lang.String eventId;
	/**创建时间*/
	private java.util.Date createDate;
	/**参数*/
	private java.lang.String param;
	/**开始处理时间*/
	private java.util.Date beginTime;
	/**处理结束时间*/
	private java.util.Date endTime;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  唯一标识
	 */
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  唯一标识
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  事件ID
	 */
	public java.lang.String getEventId(){
		return this.eventId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  事件ID
	 */
	public void setEventId(java.lang.String eventId){
		this.eventId = eventId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  参数
	 */
	public java.lang.String getParam(){
		return this.param;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数
	 */
	public void setParam(java.lang.String param){
		this.param = param;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  开始处理时间
	 */
	public java.util.Date getBeginTime(){
		return this.beginTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  开始处理时间
	 */
	public void setBeginTime(java.util.Date beginTime){
		this.beginTime = beginTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  处理结束时间
	 */
	public java.util.Date getEndTime(){
		return this.endTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  处理结束时间
	 */
	public void setEndTime(java.util.Date endTime){
		this.endTime = endTime;
	}
}
