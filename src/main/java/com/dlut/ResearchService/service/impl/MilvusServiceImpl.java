package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.service.IMilvusService;
import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.SearchResults;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.dml.SearchParam;
import jakarta.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MilvusServiceImpl implements IMilvusService {
    final Integer SEARCH_K = 10;
    final String SEARCH_PARAM = "{\"nprobe\":10, \"offset\":0}";
    @Resource
    private MilvusServiceClient milvusServiceClient;

    @Override
    public Set<Integer> searchByString(String query, String collectionName) {
        milvusServiceClient.loadCollection(
        LoadCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .build()
        );
        List<String> search_output_fields = List.of("{}_id");
        List<List<Float>> search_vectors = List.of(Arrays.asList(0.1f, 0.2f));

        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName("book")
                .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                .withMetricType(MetricType.L2)
                .withOutFields(search_output_fields)
                .withTopK(SEARCH_K)
                .withVectors(search_vectors)
                .withVectorFieldName("book_intro")
                .withParams(SEARCH_PARAM)
                .build();
        return null;
    }



}
