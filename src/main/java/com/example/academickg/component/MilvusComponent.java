package com.example.academickg.component;

import com.example.academickg.entity.constants.MilvusConstants;
import com.google.common.collect.Lists;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.grpc.SearchResults;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import io.milvus.param.partition.CreatePartitionParam;
import jakarta.annotation.Resource;
import io.milvus.param.RpcStatus;
import io.milvus.grpc.GetIndexBuildProgressResponse;
import io.milvus.param.index.GetIndexBuildProgressParam;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
public class MilvusComponent {
    private static final Logger logger = LoggerFactory.getLogger(MilvusComponent.class);
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
    public R<SearchResults> query(List<?> list, Integer num, Integer orgId){
        SearchParam.Builder builder = SearchParam.newBuilder()
                //集合名称
                .withCollectionName(MilvusConstants.COLLECTION_NAME)
                //计算方式
                // 欧氏距离 (L2)
                // 内积 (IP)
                .withMetricType(MetricType.IP)
                //返回多少条结果
                .withTopK(num)
                //搜索的向量值
                .withVectors(list)
                //搜索的Field
                .withVectorFieldName(MilvusConstants.Field.ARCHIVE_FEATURE)
                //https://milvus.io/cn/docs/v2.0.0/performance_faq.md
                .withParams("{\"nprobe\":512}");
        if (orgId != null) {
            //如果只需要搜索某个分区的数据,则需要指定分区
            builder
                    .withExpr(MilvusConstants.Field.ORG_ID + " == " + orgId)
                    .withPartitionNames(Lists.newArrayList(MilvusConstants.getPartitionName(orgId)));
        }
        return milvusServiceClient.search(builder.build());
    }




}
