package com.example.academickg.component;

import com.example.academickg.entity.constants.Result;
import com.example.academickg.entity.constants.StatusCode;

import org.springframework.stereotype.Component;

/**
 * 接口统一返回包装类
 */
@Component("resultBuilder")
public class ResultBuilder {
    public Result build(StatusCode statusCode, String msg, Object data) {
        Result result = new Result();
        result.setStatusCode(statusCode);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public Result build(StatusCode statusCode, String msg) {
        Result result = new Result();
        result.setStatusCode(statusCode);
        result.setMsg(msg);
        return result;
    }
}
