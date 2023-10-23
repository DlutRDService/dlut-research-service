package com.dlut.ResearchService.entity.dto;

import lombok.Data;

@Data
public class AuthorDto {
    private String name;

    private String country;

    private String organization;

    private String paperPeerYear;

    private Double H;

    private Integer paperNum;
}
