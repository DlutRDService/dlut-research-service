package com.dlut.ResearchService.entity.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zsl
 * @since 2023-07-12
 */
@TableName("email_code")
@Data
public class EmailCode{

    @ApiModelProperty()
    private String email;
    @ApiModelProperty()
    private String code;
    @ApiModelProperty()
    private Date createTime;
    @ApiModelProperty()
    private Integer status;


}
