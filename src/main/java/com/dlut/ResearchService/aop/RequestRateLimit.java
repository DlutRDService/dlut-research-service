package com.dlut.ResearchService.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@Aspect
@Component("requestRateLimit")
public class RequestRateLimit {
    @Pointcut("@annotation(com.dlut.ResearchService.annotation.RequestRateLimit)")
    private void requestInterceptor() {
    }
    @Before("requestInterceptor()")
    public void interceptorDo() throws Throwable {
        // 获取当前请求的HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();
        if (!isAccessLimit(session)){
            log.error("访问过于频繁，稍后再试");
            throw new Exception("访问过于频繁，稍后再试");
        }
    }
    private boolean isAccessLimit(@NotNull HttpSession session) {
        Long lastAccessedTime = session.getLastAccessedTime();
        Long currentTime = System.currentTimeMillis();
        int accessCount = session.getAttribute("accessCount") == null? 1 : (int) session.getAttribute("accessCount") + 1;
        if (currentTime - lastAccessedTime < 1000 && accessCount > 2){
            return false;
        }
        return true;
    }



}
