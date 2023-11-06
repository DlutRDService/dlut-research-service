package com.dlut.ResearchService.entity.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Paper {
    @ApiModelProperty(value = "作者id")
    private Integer paper_id;

    @ApiModelProperty(value = "标题")
    private String tl;

    @ApiModelProperty(value = "作者")
    private String au;

    @ApiModelProperty(value = "关键词")
    private String de;

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
    private Integer nr;

    @ApiModelProperty(value = "论文摘要")
    private String ab;
}
