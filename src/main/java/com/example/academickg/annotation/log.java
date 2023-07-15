package com.example.academickg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.PARAMETER, ElementType.METHOD})  //@Target注解定义注解的使用位置，如果没有该项，表示注解可以是用到任何位置
@Retention(RetentionPolicy.RUNTIME)  //确定注解的生命周期
public @interface log {
    // 注明名称
    String name() default "";
}
