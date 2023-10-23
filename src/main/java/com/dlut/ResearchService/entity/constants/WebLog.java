package com.dlut.ResearchService.entity.constants;

import lombok.Data;

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
