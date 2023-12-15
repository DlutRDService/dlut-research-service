package com.dlut.ResearchService.aspect;

import com.dlut.ResearchService.entity.dao.WebLog;
import com.dlut.ResearchService.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component("logAspect")
public class LogAspect {
    @Pointcut("@annotation(com.dlut.ResearchService.annotation.log)")
    private void logPointCut(){}
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning() {
        saveLog();
    }
    @AfterThrowing(value = "logPointCut()")
    public void doAfterThrowing() {
        saveLog();
    }

    private void saveLog(){
        WebLog webLog = new WebLog();
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = attributes.getRequest();

            webLog.setOps_ip(IpUtils.getIpAddr(request));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            webLog.setOps_time(dateFormat.format(new Date()));

            String methodName = request.getMethod();
            webLog.setRequest_method(methodName);

            String url = request.getRequestURI();
            webLog.setOps_url(url);
            Map<String, String[]> parameterMap = request.getParameterMap();
            String paramsJson = parameterMap.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                    .collect(Collectors.joining(", ", "{", "}"));
            webLog.setOps_param(paramsJson);

        } catch (Exception ex) {
            // TODO log是如何记录到本地异常日志的呢？
            log.error("异常信息 : {}", ex.getMessage());
        }
        log.info("ip地址：{}, 访问时间：{}, 请求方法：{}, 调用接口：{}, 请求参数：{}",
                webLog.getOps_ip(), webLog.getOps_time(), webLog.getRequest_method(), webLog.getOps_url(), webLog.getOps_param());
    }
}
