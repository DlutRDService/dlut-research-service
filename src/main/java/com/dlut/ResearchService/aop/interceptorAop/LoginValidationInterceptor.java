package com.dlut.ResearchService.aop.interceptorAop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component("loginValidationInterceptor")
public class LoginValidationInterceptor {

    @Pointcut("execution(* com.dlut.ResearchService.controller.*.*(..)) " +
            "&& !execution(* com.dlut.ResearchService.controller.LoginController.*(..))")
    private void loginValidationPointcut(){
    }
    @Around("loginValidationPointcut()")
    public Object aroundMethodExecution(ProceedingJoinPoint point) throws Throwable {
        // 在目标方法执行前进行登录验证逻辑
        // 检查用户是否已登录，如果未登录则阻止进一步处理请求

        // 执行目标方法

        // 在目标方法执行后进行处理，如果需要的话

        return point.proceed();
    }
}
