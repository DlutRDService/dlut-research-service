package com.dlut.ResearchService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;


@Configuration
public class Neo4jConfig {
    @Value("${neo4j.url:#{\"blot://127.0.0.1:7687\"}}")
    private String url;  // 所在服务器地址
    @Value("${neo4j.username:#{\"neo4j\"}}")
    private String user;  // 用户名
    @Value("${neo4j.password:#{\"dlutaiservice\"}}")
    private String password;  //密码
    @Bean
    public Driver driver(){
        return GraphDatabase.driver(url, AuthTokens.basic(user, password));
    }
}
