package com.example.academickg.service;

import com.example.academickg.entity.dto.PaperSimilarityDto;

public interface IMilvusService {
    PaperSimilarityDto paperSimilarity(double[] features, Integer orgId, int num);
}
