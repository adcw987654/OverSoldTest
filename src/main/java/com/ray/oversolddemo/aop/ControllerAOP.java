package com.ray.oversolddemo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerAOP {

    @Around("execution(* com.ray.oversolddemo.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("方法名稱:{}, 執行時間:{},", methodName, executionTime);
        } catch (Throwable e) {
            log.error("呼叫{}發生錯誤!", methodName);
        }
        return result;
    }

}
