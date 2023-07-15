package com.example.academickg.entity.dao;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author zsl
 * @since 2023-06-03
 */
@ApiModel(value = "Author对象", description = "")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String country;

    private String organization;

    private Double h;

    private Integer paperNum;

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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public Integer getPaperNum() {
        return paperNum;
    }

    public void setPaperNum(Integer paperNum) {
        this.paperNum = paperNum;
    }

    @Override
    public String toString() {
        return "Author{" +
        "id = " + id +
        ", name = " + name +
        ", country = " + country +
        ", organization = " + organization +
        ", h = " + h +
        ", paperNum = " + paperNum +
        "}";
    }
}
