package com.example.academickg.constants;

public class MilvusConstants {
    /**
     * 集合名称(库名)
     */
    public static final String COLLECTION_NAME = "materiel_feature_one";
    /**
     * 分片数量
     */
    public static final Integer SHARDS_NUM = 8;
    /**
     * 分区数量
     */
    public static final Integer PARTITION_NUM = 10;

    /**
     * 分区前缀
     */
    public static final String PARTITION_PREFIX = "shards_";
    /**
     * 特征值长度
     */
    public static final Integer FEATURE_DIM = 256;

    /**
     * 字段
     */
    public static class Field {
        /**
         * 主键id
         */
        public static final String ARCHIVE_ID = "feature_id";
        /**
         * 物料id
         */
        public static final String ORG_ID = "materiel_id";
        /**
         * 特征值
         */
        public static final String ARCHIVE_FEATURE = "archive_feature";
    }
    /**
     * 通过组织id计算分区名称
     */
    public static String getPartitionName(Integer orgId) {
        return PARTITION_PREFIX + (orgId % PARTITION_NUM);
    }
}
