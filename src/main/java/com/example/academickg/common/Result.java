package com.example.academickg.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.academickg.entity.constants.StatusCode;
import org.springframework.stereotype.Component;

/**
 * 接口统一返回包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component("result")
public class Result {
    private StatusCode statusCode;
    private String msg;
    private Object data;

    public static Result success(String msg, Object data){
        return new Result(StatusCode.STATUS_CODE_200, msg, data);
    }
    public static Result systemError(String msg, Object data){
        return new Result(StatusCode.STATUS_CODE_500, msg, data);
    }
    public static Result permissionsError(String msg, Object data){
        return new Result(StatusCode.STATUS_CODE_401, msg, data);
    }
    public static Result paramError(String msg, Object data){
        return new Result(StatusCode.STATUS_CODE_400, msg, data);
    }
}
