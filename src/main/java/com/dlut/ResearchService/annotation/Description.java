package com.dlut.ResearchService.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Description {
    String value();
    String example() default "";
}
