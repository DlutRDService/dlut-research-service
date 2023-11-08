package com.dlut.ResearchService.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component("loginValidationInterceptor")
public class LoginValidationInterceptor {

    @Pointcut("execution(* com.dlut.ResearchService.controller.*.*.*(..)) " +
            "&& !execution(* com.dlut.ResearchService.controller.userController.LoginController.*(..))")
    public void loginValidationPointcut(){
    }
    @Before("loginValidationPointcut()")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                                         .getRequest();
        HttpSession session =  request.getSession();
        if(!isUserLoggedIn(session)){
            log.error("用户未登陆或会话已过期");
            throw new RuntimeException("用户未登陆或会话已过期");
        }
    }
    private boolean isUserLoggedIn(HttpSession session) {
        if (session == null) {
            return false;
        }
        if (session.getAttribute("sessionID") == null) {
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
