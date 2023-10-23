package com.dlut.ResearchService.entity.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Data
@TableName(value = "author")
public class Author {
    @TableId(value = "id")
    private Integer id;

    private String name;

    private String country;

    private String organization;

    private String paperPeerYear;

    private Double H;

    private Integer paperNum;
    private Integer authenticate;

}
