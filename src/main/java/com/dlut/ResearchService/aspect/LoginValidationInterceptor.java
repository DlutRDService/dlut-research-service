package com.dlut.ResearchService.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@Aspect
@Component("loginValidationInterceptor")
public class LoginValidationInterceptor {

    @Pointcut("execution(* com.dlut.ResearchService.controller.*.*.*(..)) " +
            "&& !execution(* com.dlut.ResearchService.controller.dataController.TextAnalysisController.*(..))")
    public void loginValidationPointcut(){
    }

    @Before("loginValidationPointcut()")
    public void beforeMethodExecution(){
        HttpServletRequest request = (
                (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
        ).getRequest();
        HttpSession session =  request.getSession();
        if(!isUserLoggedIn(session)){
            throw new RuntimeException("请先登录");
        }
    }
    @Around("loginValidationPointcut()")
    public Object aroundMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            HttpServletResponse response = (
                    (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
            ).getResponse();
            if (response != null) {
                response.sendRedirect("/login");
            } else {
                throw new IllegalStateException("Response对象为空");
            }
            return null;
        }
    }

    private boolean isUserLoggedIn(HttpSession session) {
        if (session == null) {
            return false;
        }
        if (session.getAttribute("sessionID") == null) {
            return false;
        }
        return !session.isNew()
                && session.getMaxInactiveInterval() > 0
                && session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000L >= System.currentTimeMillis();
    }
}
