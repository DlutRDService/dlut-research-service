package com.dlut.ResearchService.entity.constants;

import lombok.Data;

/**
 * 接口统一返回包装类
 */
@Data
public class Result {
    private StatusCode statusCode;
    private String msg;
    private Object data;
}
