package com.example.academickg.entity.dao;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Data
public class Author implements Serializable {
    @TableId(value = "id")
    private Integer id;

    private String name;

    private String country;

    private String organization;

    private String paperPeerYear;

    private Double H;

    private Integer paperNum;

}
