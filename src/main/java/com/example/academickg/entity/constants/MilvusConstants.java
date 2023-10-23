package com.example.academickg.entity.constants;

public class MilvusConstants {
    // 集合名称
    public static final String COLLECTION_NAME = "AI";
    // 分片数量
    public static final Integer SHARDS_NUM = 8;
    // 分区数量
    public static final Integer PARTITION_NUM = 10;
    // 分区前缀
    public static final String PARTITION_PREFIX = "shards_";
    // 特征维度
    public static final Integer FEATURE_DIM = 4096;
    // 字段
    public static class Field {
        // 主键
        public static final String ARCHIVE_ID = "feature_id";
        // 组织id
        public static final String ORG_ID = "materiel_id";
        // 特征向量
        public static final String ARCHIVE_FEATURE = "archive_feature";
    }
}
