/**
 * 
 */
package com.ptnetwork.enterpriseSms.client.task.handler;

import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import com.ptnetwork.enterpriseSms.client.cache.CacheManager;
import com.ptnetwork.enterpriseSms.client.common.UUIDGenerator;
import com.ptnetwork.enterpriseSms.client.domain.BillRequest;
import com.ptnetwork.enterpriseSms.client.domain.ChannelInstruct;
import com.ptnetwork.enterpriseSms.client.domain.ChannelUser;
import com.ptnetwork.enterpriseSms.client.domain.Deliver;
import com.ptnetwork.enterpriseSms.client.queue.QueueManager;
import com.ptnetwork.enterpriseSms.protocol.DeliverPacket;

/**
 * @author jasonzhang
 *
 */
public class ReceiveMsgHandler {

	private static final Logger logger = Logger
			.getLogger(ReceiveMsgHandler.class);

	private final static ReceiveMsgHandler instance = new ReceiveMsgHandler();

	public final static ReceiveMsgHandler getInstance() {
		return instance;
	}

	private ReceiveMsgHandler() {
	}

	public void handle(DeliverPacket data) throws HandlerException {
		// 此处获取到的信息瞪部都是下发短信，其它类型的包已经被内核模块处理过了
		// 转成deliver对象放入短信队列
		Deliver deliver = this.convertDeliver(data);
		QueueManager.getInstance().addDeliver(deliver);
		// 是否状态报告
		if (deliver.isReport()) {
			// 是状态报告,不处理
			logger.info("this is status report no process");
		} else {
			// 先判断手机号码是否是数字,如果不是数字则不处理并写入日志
			if (NumberUtils.isDigits(deliver.getSrcTermId())) {
				//短信内容是否带有#号
				String msgContent = deliver.getMsgContent();
				int idx = msgContent.indexOf('#');
				if (idx > 0) {
					msgContent = msgContent.substring(0, idx);
				}
				// 是否渠道指令
				ChannelUser channelUser = CacheManager.getInstance()
						.getChannelUser(msgContent);
				if (channelUser != null) {
					Date currentDate = new Date();
					BillRequest billRequest = new BillRequest();
					billRequest.setId((new UUIDGenerator()).generate());
					billRequest.setUserName(channelUser.getUsername());
					billRequest.setUserPass(channelUser.getPassword());
					billRequest.setInstruct(deliver.getMsgContent());
					billRequest.setMsisdn(new Long(deliver.getSrcTermId()));
					billRequest.setState(0);
					billRequest.setStartTime(currentDate);
					billRequest.setEndTime(currentDate);
					billRequest.setCreateDate(currentDate);
					billRequest.setDeliverId(deliver.getId());
					//设置posturl
					String postUrl = CacheManager.getInstance().getPostUrl(channelUser.getChannelId());
					if (postUrl != null) {
						billRequest.setPostUrl(postUrl);
					}
					BillRequestHandler.getInstance().handle(billRequest);
					QueueManager.getInstance().addNewBillRequest(billRequest);
				} else {
					logger.info("this is not channel instruct with msg "
							+ deliver.getMsgContent());
				}
			} else {
				logger.warn("this srctermid is not long number with "
						+ deliver.getSrcTermId());
			}

		}
	}

	private Deliver convertDeliver(DeliverPacket deliverPacket) {
		Deliver deliver = new Deliver();
		deliver.setId((new UUIDGenerator()).generate());
		deliver.setMsgId(deliverPacket.getMsgId());
		deliver.setCreateDate(new Date());
		deliver.setDestTermId(deliverPacket.getDestTermId());
		deliver.setMsgContent(deliverPacket.getMsgContent());
		deliver.setMsgFormat(deliverPacket.getMsgFormat());
		deliver.setMsgLength(deliverPacket.getMsgLength());
		deliver.setReport(deliverPacket.getIsReport() == 1 ? true : false);
		deliver.setServiceId(deliverPacket.getServiceId());
		deliver.setSrcTermId(deliverPacket.getSrcTermId().trim());
		deliver.setState(0);
		return deliver;
	}
}
