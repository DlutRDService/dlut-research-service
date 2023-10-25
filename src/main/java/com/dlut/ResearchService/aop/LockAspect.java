package com.dlut.ResearchService.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component("lockAspect")
@Aspect
@Slf4j
public class LockAspect {
    private static final Lock lock = new ReentrantLock();

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.dlut.ResearchService.annotation.lock)")
    private void lockPointCut(){}

    /**
     * 环绕增强
     */
    @Around(value = "lockPointCut()")
    public Object around(ProceedingJoinPoint joinPoint){
        lock.lock();
        //log.info("lock执行了");
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("获取锁失败");
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return result;
    }


}
