package com.dlut.ResearchService.entity.constants;

public class FlaskUrl {
    public static final String BASE_URL = "http://127.0.0.1:5000/";
    public static final String TEXT_IMPORT_TO_MYSQL_URL = BASE_URL + "api/import_mysql";
    public static final String TEXT_IMPORT_TO_NEO4J_URL = BASE_URL + "api/import_neo4j";
    public static final String TEXT_IMPORT_TO_MILVUS_URL = BASE_URL + "api/import_milvus";
    public static final String TEXT_CLASS_URL = BASE_URL + "api/classification";
    public static final String TEXT_NER_URL = BASE_URL + "api/ner";
    public static final String TEXT_SEQUENCE_URL = BASE_URL + "api/sequence";
    public static final String TEXT_SENTIMENT_URL = BASE_URL + "api/sentiment";
    public static final String TEXT_QA_URL = BASE_URL + "api/question_answering";
}
