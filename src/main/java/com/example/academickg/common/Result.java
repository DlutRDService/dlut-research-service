package com.example.academickg.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.academickg.entity.constants.StatusCode;
import org.springframework.stereotype.Component;

/**
 * 接口统一返回包装类
 */
@Data
@NoArgsConstructor
@Component("result")
public class Result {
    private StatusCode statusCode;
    private String msg;
    private Object data;
    public Result(StatusCode statusCode, String msg, Object data){
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }
    public Result(StatusCode statusCode, String msg){
        this.statusCode = statusCode;
        this.msg = msg;
    }
    public Result changeResultState(Result result,StatusCode statusCode, String msg, Object data){
        result.setStatusCode(statusCode);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    public Result changeResultState(Result result,StatusCode statusCode, String msg){
        result.setStatusCode(statusCode);
        result.setMsg(msg);
        return result;
    }
}
