package com.example.academickg.exception;

import com.example.academickg.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;

public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 异常对应返回玛
     */
    private Integer code;

    /**
     * 异常对应描述信息
     */
    private String message;

    private Throwable throwable;

    public BusinessException(){
        super();
    }

    public BusinessException(String message){
        super(message);
        this.message = message;
    }

    public BusinessException(String message, Integer code){
        super(message);
        this.message = message;
        this.code = code;
    }

    public BusinessException(String message, Throwable cause){
        super(message, cause);
        this.message = String.format("%s %s", message, cause.getMessage());
    }

    public BusinessException(int code, String message, Throwable throwable) {
        super(message);
        this.code = code;
        this.message = message;
        this.throwable = throwable;
    }

//    public BusinessException(Result result){
//        this(result.getStatusCode(), result.getData(), null);
//    }

}

