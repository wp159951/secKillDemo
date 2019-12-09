package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.mns.model.Message;
import com.demo.dto.RespModel;
import com.demo.dto.TestDTO;
import com.demo.service.AliQueueServiceImpl;
import com.demo.service.SchoolServiceImpl;
import com.demo.vo.TestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.atomic.AtomicInteger;


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

    @Autowired
    private SchoolServiceImpl schoolService;

    private int count = 5;



    /**
     * valid测试
     * @param dto
     * @return
     */
    @PostMapping("test")
    public RespModel<TestVO> info(@Valid @RequestBody TestDTO dto){
        RespModel respModel = new RespModel();
        TestVO data = new TestVO();
        data.setJsonStr(JSON.toJSONString(dto));
        respModel.setData(data);
        return respModel;
    }

    /**
     * 秒杀入口
     * 入口不做处理，直接发送到消息队列
     * 消息队列的方式
     * @param userId
     * @return
     */
//    @GetMapping("secKill")
//    public RespModel secKill(@RequestParam("userId") String userId){
//        aliQueueService.sendMessage(userId);
//        RespModel respModel = new RespModel();
//        return respModel;
//    }

    /**
     * 同步接口方式  效率超级低
     * @param userId
     * @return
     */
//    @GetMapping("secKill")
//    synchronized public RespModel secKill(@RequestParam("userId") String userId){
//        RespModel respModel = new RespModel("fail");
//        if(count > 0){
//            System.out.println(userId);
//            count--;
//            respModel = new RespModel();
//        }
//        return respModel;
//    }


    private static AtomicInteger atomicCount = new AtomicInteger(0);

    private static final Integer total = 5;

    /**
     * 使用原子的计数类
     *
     * @param userId
     * @return
     */
    @GetMapping("secKill")
    public RespModel secKill(@RequestParam("userId") String userId){
        RespModel respModel = new RespModel("fail");
        int now = atomicCount.incrementAndGet();
        if(now <= total){
            System.out.println(userId+"------"+now);
            respModel = new RespModel();
        }
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

    @GetMapping("school")
    public RespModel getSchools(){
        schoolService.getSchools();
        return new RespModel();
    }

}
