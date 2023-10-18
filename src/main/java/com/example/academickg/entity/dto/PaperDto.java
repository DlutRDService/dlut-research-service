package com.example.academickg.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaperDto {
    private Integer id;
    private String Title;
    private List<Object> Authors;
    private List<String> Keywords;
    private Integer Year;
    private String Journal;
    private String esi;
    private String wc;
}
