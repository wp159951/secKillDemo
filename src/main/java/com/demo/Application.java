package com.demo;

import com.demo.controller.TestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created with IntelliJ IDEA
 * USER: yangzhizhuang
 * Date: 2019/7/4
 * Description:
 */
@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private TestController testController;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        testController.receive();
    }
}
