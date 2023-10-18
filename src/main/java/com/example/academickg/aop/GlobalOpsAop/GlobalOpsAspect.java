package com.example.academickg.aop.GlobalOpsAop;

import com.example.academickg.annotation.GlobalInterceptor;
import com.example.academickg.annotation.VerifyParams;
import com.example.academickg.entity.constants.StatusCode;
import com.example.academickg.exception.BusinessException;
import com.example.academickg.utils.JsonUtils;
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

@Aspect
@Slf4j
@Component("globalOpsAspect")
public class GlobalOpsAspect {

    private static final String[] TYPE_BASE = {"java.lang.String", "java.lang.Integer","java.lang.Long"};

    @Pointcut("@annotation(com.example.academickg.annotation.GlobalInterceptor)")
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
            // 校验登陆
//            if (interceptor.checkLogin()) {
//
//            }
            // 校验参数
            if (interceptor.checkParams()){
                validateParams(method, args);
            }
            return point.proceed();
        } catch (BusinessException e){
            log.error("全局拦截器异常", e);
            throw e;
        } catch (Throwable e){
            log.error("全局拦截器异常", e);
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
            log.info(JsonUtils.toJson(value));
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
