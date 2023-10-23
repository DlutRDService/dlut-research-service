package com.dlut.ResearchService.entity.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Data
public class Keywords{
    @ApiModelProperty(value = "关键词id")
    private Integer keyword_id;

    @ApiModelProperty(value = "关键词名")
    private String keyword_name;

    @ApiModelProperty(value = "频次")
    private Integer occurNums;

    @ApiModelProperty(value = "研究领域")
    private String relatedResearch;
}

