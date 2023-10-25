package com.dlut.ResearchService.aop;

import com.alibaba.fastjson.JSON;
import com.dlut.ResearchService.entity.constants.WebLog;
import com.dlut.ResearchService.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("logAspect")
@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 日志切入点
     */
    @Pointcut("@annotation(com.dlut.ResearchService.annotation.log)")
    private void logPointCut(){}

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        saveLog(joinPoint);
    }

    /**
     * 拦截异常操作
     * @param joinPoint 切点
     */
    @AfterThrowing(value = "logPointCut()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        saveLog(joinPoint);
    }

    private void saveLog(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        //获取Ip地址
        WebLog webLog = new WebLog();
        webLog.setIp(IpUtils.getIpAddr(request));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        webLog.setCreate_time(dateFormat.format(new Date()));

        String methodName = signature.getName();
        webLog.setMethod(methodName);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            List<String> list = new ArrayList<>();
            for (Object o : args) {
                list.add(JSON.toJSONString(o));
            }
            webLog.setParameter(list.toString());
        } catch (Exception ex) {
            // 记录本地异常日志
            log.error("异常信息 : {}", ex.getMessage());
        }
        log.info("ip地址：{},访问时间：{},调用方法：{}",
                webLog.getIp(), webLog.getCreate_time(), webLog.getMethod());
    }
}
