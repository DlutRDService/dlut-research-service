package com.example.academickg.entity.dao;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-07-12
 */
@TableName("user_info")
@ApiModel(value = "UserInfo对象", description = "")
@Data
public class UserInfo implements Serializable {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("账号")
    private Integer account;

    @ApiModelProperty("头像")
    private byte[] avatar;

    @ApiModelProperty("邮箱(****@mail.dlut.edu.cn\\@dlut.edu.cn)")
    private String email;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("注册时间")
    private LocalDateTime registrationTime;

    @ApiModelProperty("上次登陆时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("状态 0:禁用 1:启动")
    private Boolean status;
}
