package com.dlut.ResearchService.entity.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Data
public class Author {
    @ApiModelProperty(value = "作者ID")
    private Integer author_id;

    @ApiModelProperty(value = "作者姓名")
    private String author_name;

    @ApiModelProperty(value = "作者国别")
    private String author_country;

    @ApiModelProperty(value = "研究领域")
    private String researchKeywords;

    @ApiModelProperty(value = "作者机构")
    private String author_organization;

    @ApiModelProperty(value = "每年发文")
    private String paperPeerYear;

    @ApiModelProperty(value = "H指数")
    private Integer H;

    @ApiModelProperty(value = "发文数量")
    private Integer paperNums;

    @ApiModelProperty(value = "是否认证")
    private Integer authenticated;

}
