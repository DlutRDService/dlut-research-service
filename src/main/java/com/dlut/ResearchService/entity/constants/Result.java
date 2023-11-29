package com.dlut.ResearchService.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private StatusCode statusCode;
    private String msg;
    private Object data;
}
