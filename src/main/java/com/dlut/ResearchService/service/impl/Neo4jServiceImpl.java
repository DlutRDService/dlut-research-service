package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.service.INeo4jService;
import jakarta.annotation.Resource;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.neo4j.driver.Values.parameters;

@Service
public class Neo4jServiceImpl implements INeo4jService {
    @Resource
    private ResultBuilder resultBuilder;
    @Resource
    private Driver driver;
    @Override
    public Result getCoAuthorIds(Integer author_id) {
        List<Integer> data = null;
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "Success", null);
    }

    @Override
    public Result queryRelatedGraph(Integer id, String cypher) {
        try (var session = driver.session()) {
            var greeting = session.executeRead(tx -> {
                var query = new Query(cypher);
                // TODO 搞一下返回的查询结果类型
                return tx.run(query).stream().toList();
            });
            System.out.println(greeting);
        }
        return null;
    }
}
