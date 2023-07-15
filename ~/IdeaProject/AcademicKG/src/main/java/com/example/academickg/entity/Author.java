package com.example.academickg.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author Zsl
 * @since 2023-05-19
 */
@ApiModel(value = "Author对象", description = "")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String country;

    private String orginization;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrginization() {
        return orginization;
    }

    public void setOrginization(String orginization) {
        this.orginization = orginization;
    }

    @Override
    public String toString() {
        return "Author{" +
        "id = " + id +
        ", name = " + name +
        ", country = " + country +
        ", orginization = " + orginization +
        "}";
    }
}
