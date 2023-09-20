package com.example.academickg.service.impl;

import com.example.academickg.entity.constants.MilvusConstants;
import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.DataType;
import io.milvus.grpc.GetIndexBuildProgressResponse;
import io.milvus.grpc.SearchResults;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import io.milvus.param.index.GetIndexBuildProgressParam;
import io.milvus.param.partition.CreatePartitionParam;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

@Service
public class MilvusServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(MilvusServiceImpl.class);
    @Resource
    private MilvusServiceClient milvusServiceClient;

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
     * 创建集合
     */
    public R<RpcStatus> createCollection(String collectionName){
        if(isCollection(collectionName)) {
            logger.error("该集合已经存在，请检查。");
            return null;
        }
        FieldType archiveId = FieldType.newBuilder()
                .withName(MilvusConstants.Field.ARCHIVE_ID)
                .withDescription("主键id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(false)
                .build();
        FieldType orgId = FieldType.newBuilder()
                .withName(MilvusConstants.Field.ORG_ID)
                .withDescription("组织id")
                .withDataType(DataType.Int32)
                .build();
        FieldType archiveFeature = FieldType.newBuilder()
                .withName(MilvusConstants.Field.ARCHIVE_FEATURE)
                .withDescription("特征值")
                .withDataType(DataType.FloatVector)
                .withDimension(MilvusConstants.FEATURE_DIM)
                .build();
        CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                .withCollectionName(MilvusConstants.COLLECTION_NAME)
                .withDescription("集合")
                .withShardsNum(MilvusConstants.SHARDS_NUM)
                .addFieldType(archiveId)
                .addFieldType(orgId)
                .addFieldType(archiveFeature)
                .build();
        return milvusServiceClient.createCollection(createCollectionReq);
    }


    /**
     * 创建分区
     */
    private void createPartition(String collectionName, String partitionName) {
        R<RpcStatus> response = milvusServiceClient.createPartition(CreatePartitionParam.newBuilder()
                .withCollectionName(collectionName) //集合名称
                .withPartitionName(partitionName) //分区名称
                .build());
    }
    /**
     * 创建索引
     */
    public R<RpcStatus> createIndex() {
        R<RpcStatus> response = milvusServiceClient.createIndex(CreateIndexParam.newBuilder()
                .withCollectionName(MilvusConstants.COLLECTION_NAME)
                .withFieldName(MilvusConstants.Field.ARCHIVE_FEATURE)
                .withIndexType(IndexType.IVF_FLAT)
                .withMetricType(MetricType.IP)
                //nlist 建议值为 4 × sqrt(n)，其中 n 指 segment 最多包含的 entity 条数。
                .withExtraParam("{\"nlist\":16384}")
                .withSyncMode(Boolean.FALSE)
                .build());
        logger.info("createIndex-------------------->{}", response.toString());
        R<GetIndexBuildProgressResponse> idnexResp = milvusServiceClient.getIndexBuildProgress(
                GetIndexBuildProgressParam.newBuilder()
                        .withCollectionName(MilvusConstants.COLLECTION_NAME)
                        .build());
        logger.info("getIndexBuildProgress---------------------------->{}", idnexResp.toString());
        return response;
    }

    /**
     * 查询
     */
    public R<SearchResults> query(List<Float> list, Integer TOP_K){
        final String SEARCH_PARAM = "{\"nprobe\":10, \"offset\":5}";    // Params
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



}
