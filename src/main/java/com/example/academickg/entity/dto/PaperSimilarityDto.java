package com.example.academickg.entity.dto;

import lombok.Data;

@Data
public class PaperSimilarityDto {
    private long id;
    private float source;
    public PaperSimilarityDto(long id, float source){
        this.source = source;
        this.id = id;
    }
}
