package com.example.academickg.Aop.logAspect;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

/**
 * 日志封装类
 */
@Data
public class WebLog {
    private String ip;
    private String create_time;
    private String description;
    private String username;
    private String basePath;
    private String url;
    private String method;
    private Object parameter;
    private Object result;

}
