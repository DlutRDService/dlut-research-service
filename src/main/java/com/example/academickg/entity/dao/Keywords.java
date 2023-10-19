package com.example.academickg.entity.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Data
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

