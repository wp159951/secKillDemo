package com.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA
 *
 * @author: yangzhizhuang
 * @date: 2019/11/26
 * @description:
 */
@Configuration
public class AliQueueConfig {

    @Value("${access.key.id}")
    private String accessKeyId = "LTAI4Fof8mk5s4LHruo1xcxQ";
    @Value("${access.key.secret}")
    private String accessKeySecret = "LFUiqLOng1scaoThXcambc5wW0ugni";
    @Value("${account.end.point}")
    private String accountEndPoint = "https://1279882918531483.mns.cn-shenzhen.aliyuncs.com/";
    @Value("${receive.queue.name}")
    private String receiveQueueName = "seckill";

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getAccountEndPoint() {
        return accountEndPoint;
    }

    public void setAccountEndPoint(String accountEndPoint) {
        this.accountEndPoint = accountEndPoint;
    }

    public String getReceiveQueueName() {
        return receiveQueueName;
    }

    public void setReceiveQueueName(String receiveQueueName) {
        this.receiveQueueName = receiveQueueName;
    }

}
