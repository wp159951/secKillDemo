package com.demo.aspect;

import com.alibaba.fastjson.JSON;
import com.demo.annotation.ToJson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 *
 * @author: yangzhizhuang
 * @date: 2019/12/6
 * @description:
 */
@Aspect
@Component
public class ToJsonAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.demo.annotation.ToJson)")
    public void logPointCut() {

    }


    @AfterReturning(value = "logPointCut()",returning = "result")
    public JSON afterMethod(JoinPoint joinpoint,Object result) throws Throwable {
        // 获得注解入参
        MethodSignature methodSignature = (MethodSignature)joinpoint.getSignature();
        Method method = methodSignature.getMethod();
        ToJson toJson = method.getAnnotation(ToJson.class);
        logger.info("注解参数:" + toJson);
        String methodName = methodSignature.getName();
        List<Object> args = Arrays.asList(joinpoint.getArgs());
        logger.info("The method " + methodName + " begins with" + args);

        return JSON.parseObject(String.valueOf(result));
    }

}
