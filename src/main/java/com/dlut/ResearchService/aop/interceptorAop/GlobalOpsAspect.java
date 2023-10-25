package com.dlut.ResearchService.aop.interceptorAop;

import com.alibaba.fastjson.JSON;
import com.dlut.ResearchService.annotation.GlobalInterceptor;
import com.dlut.ResearchService.annotation.VerifyParams;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Aspect
@Component("globalOpsAspect")
public class GlobalOpsAspect {

    private static final String[] TYPE_BASE = {"java.lang.String", "java.lang.Integer","java.lang.Long"};

    @Pointcut("@annotation(com.dlut.ResearchService.annotation.GlobalInterceptor)")
    private void requestInterceptor(){
    }
    @Around("requestInterceptor()")
    public Object interceptorDo(ProceedingJoinPoint point) throws BusinessException {
        try {
            Object target = point.getTarget();
            Object[] args = point.getArgs();
            String methodName = point.getSignature().getName();
            Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (interceptor == null) {
                return null;
            }
            if (interceptor.checkParams()){
                validateParams(method, args);
            }
            return point.proceed();
        } catch (BusinessException e){
            log.error("拦截器异常", e);
            throw e;
        } catch (Throwable e){
            log.error("拦截器异常", e);
            throw new BusinessException();
        }
    }
    private void validateParams(Method m, Object[] arguments){
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = arguments[i];
            VerifyParams verifyParams = parameter.getAnnotation(VerifyParams.class);
            if (verifyParams == null){
                continue;
            }
            if (ArrayUtils.contains(TYPE_BASE, parameter.getParameterizedType().getTypeName())){
                checkValue(value, verifyParams);
            }
            log.info(JSON.toJSONString(value));
        }
    }
    private void checkValue(Object value, VerifyParams verifyParams){
        boolean isEmpty = value == null || StringUtils.isEmpty(value.toString());
        int length = value == null ? 0 : value.toString().length();
        if (isEmpty && (verifyParams.required())){
            throw new BusinessException(StatusCode.STATUS_CODE_403, "拒绝访问");
        }
        if (! isEmpty && (verifyParams.max() != -1 && verifyParams.max() < length || verifyParams.min() != -1 && verifyParams.min() > length)){
             throw new BusinessException(StatusCode.STATUS_CODE_403, "拒绝访问");
         }
    }

}
