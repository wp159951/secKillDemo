package com.demo.service;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;
import com.demo.config.AliQueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliQueueServiceImpl {

	protected static final Logger logger = LoggerFactory.getLogger(AliQueueServiceImpl.class);


	private CloudQueue currentQueue;

	@Autowired
	public AliQueueServiceImpl(AliQueueConfig config) {
		CloudAccount account = new CloudAccount(config.getAccessKeyId(),config.getAccessKeySecret(),config.getAccountEndPoint());
		MNSClient client = account.getMNSClient();
		this.currentQueue = client.getQueueRef(config.getReceiveQueueName());
	}

	public AliQueueServiceImpl() {

	}

	/**
	 * 发送文件消息
	 * @param body
	 */
	public void sendMessage(String body) {
		try {
			Message message = new Message();
			message.setMessageBody(body);
			Message putMsg = currentQueue.putMessage(message);
			logger.info("Send message id is: " + putMsg.getMessageId());
		} catch (ClientException ce) {
			logger.error("Something wrong with the network connection between client and MNS service."
					+ "Please check your network and DNS availablity.",ce);
		} catch (ServiceException se) {
			logger.error("MNS exception requestId:" + se.getRequestId(), se);
		} catch (Exception e) {
			logger.error("Unknown exception happened!",e);
		}
	}

	/**
	 * 获取消息
	 * @return
	 */
	public  Message getMessage() {

		try {
			return currentQueue.popMessage();
		} catch (ClientException ce) {
			logger.error("Something wrong with the network connection between client and MNS service."
					+ "Please check your network and DNS availablity.",ce);
			return null;
		} catch (Exception e) {
			logger.error("Unknown exception happened!",e);
			return null;
		}
	}

	/**
	 * 删除消息
	 * @param popMsg
	 */
	public void deleteMessage(Message popMsg) {
		try {
			currentQueue.deleteMessage(popMsg.getReceiptHandle());

		} catch (ClientException ce) {
			logger.error("Something wrong with the network connection between client and MNS service."
					+ "Please check your network and DNS availablity.",ce);
		} catch (ServiceException se) {
			logger.error("MNS exception requestId:" + se.getRequestId(), se);
		} catch (Exception e) {
			logger.error("Unknown exception happened!",e);
		}
	}

}
