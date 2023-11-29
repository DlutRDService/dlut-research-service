package com.dlut.ResearchService.component;

import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.SearchResults;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.dml.SearchParam;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("milvus")
public class Milvus {

    private static final String SEARCH_PARAM = "{\"nprobe\":10, \"offset\":5}";
    private static final Integer TOP_5 = 5;
    private static final Integer TOP_10 = 10;

    @Resource
    private MilvusServiceClient milvusServiceClient;

    /**
     * 相似度查询
     */
    public R<SearchResults> queryBySimilarity(List<Float> vectorlist, Integer TOP_K, String outFieldName,
                                              String collectionName, String vectorFieldName, String params){
        List<String> search_output_fields = List.of(outFieldName);
        List<List<Float>> search_vectors = Collections.singletonList(vectorlist);
        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName(collectionName)
                .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                .withMetricType(MetricType.L2)
                .withOutFields(search_output_fields)
                .withTopK(TOP_K)
                .withVectors(search_vectors)
                .withVectorFieldName(vectorFieldName)
                .withParams(params)
                .build();
        return milvusServiceClient.search(searchParam);
    }

    /**
     * 检索作者相关论文
     */
    public R<SearchResults> searchRelatedPaper(List<Float> vectorlist, String outFieldName,
                                               String collectionName, String vectorFieldName) {
        return queryBySimilarity(vectorlist, TOP_5, outFieldName, collectionName, vectorFieldName, SEARCH_PARAM);
    }
    public List<Float> selectByIds(List<Integer> ids){
        return null;
    }
}
