package com.example.academickg.entity.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zsl
 * @since 2023-07-12
 */
@TableName("user_info")
@Data
public class UserInfo implements Serializable {
    @ApiModelProperty(value = "用户id", example = "1")
    private Integer userId;

    @ApiModelProperty(value = "昵称", example = "巴蜀爱吃的猫")
    private String nickName;

    @ApiModelProperty(value = "账号", example = "22112065")
    private Integer account;

    @ApiModelProperty(value = "头像")
    private byte[] avatar;

    @ApiModelProperty(value = "邮箱", example = "(****@mail.dlut.edu.cn\\@dlut.edu.cn)")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "注册时间", example = "2023/01/02")
    private LocalDateTime registrationTime;

    @ApiModelProperty(value = "上次登陆时间", example = "2023/01/02")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "状态", example =" 0:禁用 1:启动")
    private Boolean status;
}
