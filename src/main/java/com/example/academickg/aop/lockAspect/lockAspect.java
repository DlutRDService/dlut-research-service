package com.example.academickg.aop.lockAspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Aspect
public class lockAspect {
    private static final Lock lock = new ReentrantLock();
    //private static final Logger log = LoggerFactory.getLogger(logAspect.class);

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.example.academickg.annotation.lock)")
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
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return result;
    }


}
