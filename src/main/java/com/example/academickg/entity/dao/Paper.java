package com.example.academickg.entity.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "Paper")
public class Paper implements Serializable {
    @TableId(value = "id")
    private Integer id;

    @ApiModelProperty()
    private String tl;

    @ApiModelProperty()
    private Integer py;

    @ApiModelProperty()
    private String so;

    @ApiModelProperty()
    private String esi;

    @ApiModelProperty()
    private String wc;

    @ApiModelProperty()
    private Integer tc;

    @ApiModelProperty()
    private Integer nc;

    @ApiModelProperty()
    private String ab;
}
