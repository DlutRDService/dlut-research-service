package com.dlut.ResearchService.aop.interceptorAop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                                         .currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        if(!isUserLoggedIn(session)){
            throw new UnsupportedOperationException("用户未登陆或会话已过期");
        }
        return point.proceed();
    }
    private boolean isUserLoggedIn(HttpSession session) {
        if (session == null) {
            return false;
        }
        if (session.getAttribute("account") == null) {
            return false;
        }
        if (session.isNew()
                || session.getMaxInactiveInterval() <= 0
                || session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000
                   < System.currentTimeMillis()) {
            return false;
        }

        return true;
    }
}
