package com.example.academickg.entity.dao;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
@TableName("email_code")
@ApiModel(value = "EmailCode对象", description = "")
@Data
public class EmailCode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String email;

    private String code;

    private Date createTime;

    private Integer status;


    @Override
    public String toString() {
        return "EmailCode{" +
        "email = " + email +
        ", code = " + code +
        ", createTime = " + createTime +
        ", status = " + status +
        "}";
    }
}
