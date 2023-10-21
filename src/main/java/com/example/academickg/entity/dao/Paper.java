package com.example.academickg.entity.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "paper")
public class Paper implements Serializable {
    @TableId(value = "id")
    private Integer id;

    @ApiModelProperty(value = "标题")
    private String tl;

    @ApiModelProperty(value = "年份", example = "2017")
    private Integer py;

    @ApiModelProperty(value = "期刊", example = "Energy")
    private String so;

    @ApiModelProperty(value = "ESI类别", example = "Math")
    private String esi;

    @ApiModelProperty(value = "WC类别", example = "Artificial Intelligence")
    private String wc;

    @ApiModelProperty(value = "被引量", example = "20")
    private Integer tc;

    @ApiModelProperty(value = "引文数量", example = "3")
    private Integer nc;

    @ApiModelProperty(value = "论文摘要")
    private String ab;
}
