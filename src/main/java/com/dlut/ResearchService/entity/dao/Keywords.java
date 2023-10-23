package com.dlut.ResearchService.entity.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Data
@TableName(value = "keyword")
public class Keywords{
    @ApiModelProperty()
    private Integer id;

    @ApiModelProperty()
    private String name;

    @ApiModelProperty()
    private Integer num;

    @ApiModelProperty()
    private String research;
}

