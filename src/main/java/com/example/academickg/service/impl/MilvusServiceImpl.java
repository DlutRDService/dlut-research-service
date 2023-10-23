package com.example.academickg.service.impl;

import com.example.academickg.component.ResultBuilder;
import com.example.academickg.service.IMilvusService;
import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.SearchResults;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.dml.SearchParam;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;


@Service
public class MilvusServiceImpl implements IMilvusService {
    private static final Logger logger = LoggerFactory.getLogger(MilvusServiceImpl.class);
    private static final String SEARCH_PARAM = "{\"nprobe\":10, \"offset\":5}";
    private static final Integer TOP_5 = 5;
    private static final Integer TOP_10 = 10;


    @Resource
    private MilvusServiceClient milvusServiceClient;
    @Resource
    private ResultBuilder resultBuilder;



    /**
     * 判断集合是否存在
     */
    public Boolean isCollection(String collectionName){
        R<Boolean> response = milvusServiceClient.hasCollection(
                HasCollectionParam.newBuilder()
                        .withCollectionName(collectionName)
                        .build());
        return response.toString() != null;
    }
    /**
     * 查询
     */
    public R<SearchResults> query(List<Float> list, Integer TOP_K){
        List<String> search_output_fields = List.of("paper_id");
        List<List<Float>> search_vectors = Collections.singletonList(list);
        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName("PaperMilvus")
                .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                .withMetricType(MetricType.L2)
                .withOutFields(search_output_fields)
                .withTopK(TOP_K)
                .withVectors(search_vectors)
                .withVectorFieldName("paper_intro")
                .withParams(SEARCH_PARAM)
                .build();
        return milvusServiceClient.search(searchParam);
    }

    /**
     * 检索作者相关论文
     */
    public R<SearchResults> searchRelatedPaper(List<Float> ids) {
        List<String> search_output_fields = List.of("paper_id");
        List<List<Float>> search_vectors = Collections.singletonList(ids);
        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName("PaperMilvus")
                .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                .withMetricType(MetricType.L2)
                .withOutFields(search_output_fields)
                .withTopK(TOP_5)
                .withVectors(search_vectors)
                .withVectorFieldName("paper_intro")
                .withParams(SEARCH_PARAM)
                .build();
        return milvusServiceClient.search();
    }
    public List<Float> selectByIds(List<Integer> ids){

        return null;
    }
}



