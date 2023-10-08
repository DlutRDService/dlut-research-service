package com.example.academickg.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {
    /**
     * 是否需要校验参数
     */
    boolean checkParams() default false;
    /**
     * 是否需要登陆
     */
    boolean checkLogin() default false;

}
