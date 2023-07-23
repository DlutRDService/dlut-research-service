package com.example.academickg.service.impl;

import com.example.academickg.component.MilvusComponent;
import com.example.academickg.entity.dto.PaperSimilarityDto;
import com.example.academickg.service.IMilvusService;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import io.milvus.response.SearchResultsWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MilvusServiceImpl implements IMilvusService {
    @Resource
    private MilvusComponent milvusComponent;

    public PaperSimilarityDto paperSimilarity(double[] features, Integer orgId, int num) {
        List<Double> arrayList= Arrays.stream(features).boxed().collect(Collectors.toList());
        List<List<Double>> list = new ArrayList<>();
        list.add(arrayList);
        R<SearchResults> search = milvusComponent.query(list,2, 2);
        if (search.getData() == null) return null;
        SearchResultsWrapper wrapper = new SearchResultsWrapper(search.getData().getResults());
        for (int i = 0; i < list.size(); ++i) {
            List<SearchResultsWrapper.IDScore> scores = wrapper.getIDScore(i);
            if (scores.size() > 0) {
                System.err.println(scores);
                SearchResultsWrapper.IDScore idScore = scores.get(0);
                return new PaperSimilarityDto(idScore.getLongID(), idScore.getScore());
            }
        }
        return null;
    }


}
