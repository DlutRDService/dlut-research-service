package com.dlut.ResearchService.entity.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Data
public class Author {
    @ApiModelProperty(value = "")
    private Integer author_id;

    @ApiModelProperty(value = "")
    private String author_name;

    @ApiModelProperty(value = "")
    private String author_country;

    @ApiModelProperty(value = "")
    private String researchKeywords;

    @ApiModelProperty(value = "")
    private String author_organization;

    @ApiModelProperty(value = "")
    private String paperPeerYear;

    @ApiModelProperty(value = "")
    private Integer H;

    @ApiModelProperty(value = "")
    private Integer paperNums;

    @ApiModelProperty(value = "")
    private Integer authenticated;

}
