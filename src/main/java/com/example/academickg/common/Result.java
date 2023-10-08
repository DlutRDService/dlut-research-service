package com.example.academickg.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.academickg.constants.StatusCode;
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
    public Result(StatusCode statusCode1, String msg, Object data){
        this.statusCode = statusCode1;
        this.msg = msg;
        this.data = data;
    }
}
