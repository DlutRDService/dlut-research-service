package com.example.academickg.entity.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Data
public class Author implements Serializable {

    private Integer id;

    private String name;

    private String country;

    private String organization;

    private String paperPublishedByYear;

    private Double h;

    private Integer paperNum;

}
