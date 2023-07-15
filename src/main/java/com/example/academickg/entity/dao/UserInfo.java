package com.example.academickg.entity.dao;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("user_info")
@ApiModel(value = "UserInfo对象", description = "")
@Data
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("账户")
    private Integer account;

    @ApiModelProperty("邮箱（****@mail.dlut.edu.cn）")
    private String email;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("注册时间")
    private LocalDateTime registrationTime;

    @ApiModelProperty("上次登陆时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("状态 0:禁用 1:启动")
    private Boolean status;

    @ApiModelProperty("空间")
    private String userSpace;

    @Override
    public String toString() {
        return "UserInfo{" +
        "userId = " + userId +
        ", nickName = " + nickName +
        ", account = " + account +
        ", email = " + email +
        ", password = " + password +
        ", registrationTime = " + registrationTime +
        ", lastLoginTime = " + lastLoginTime +
        ", status = " + status +
        ", userSpace = " + userSpace +
        "}";
    }
}
