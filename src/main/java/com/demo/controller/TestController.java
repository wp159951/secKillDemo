package com.demo.controller;

import com.aliyun.mns.model.Message;
import com.demo.dto.RespModel;
import com.demo.dto.TestDTO;
import com.demo.service.AliQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created with IntelliJ IDEA
 * USER: yangzhizhuang
 * Date: 2019/7/4
 * Description:
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private AliQueueServiceImpl aliQueueService;

    /**
     * valid测试
     * @param dto
     * @return
     */
    @PostMapping("test")
    public RespModel info(@Valid @RequestBody TestDTO dto){
        return new RespModel();
    }

    /**
     * 秒杀入口
     * 入口不做处理，直接发送到消息队列
     * @param userId
     * @return
     */
    @GetMapping("secKill")
    public RespModel secKill(@RequestParam("userId") String userId){
        aliQueueService.sendMessage(userId);
        RespModel respModel = new RespModel();
        return respModel;
    }


    /**
     * 死循环拉取消息
     */
    public void receive(){
        int count = 0;
        //轮询拉取消息
        while (true){
            //获取消息
            Message message = aliQueueService.getMessage();
            if(message!=null){
                //只有5个人能中奖
                if(count < 5){
                    //打印中奖人的userId
                    System.out.println(message.getMessageBodyAsString());
                    count++;
                }
                aliQueueService.deleteMessage(message);
            }
        }
    }

}
