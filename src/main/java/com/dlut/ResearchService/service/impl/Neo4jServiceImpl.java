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
    public Result queryRelatedGraph(Integer id) {
        try (var session = driver.session()) {
            var greeting = session.executeWrite(tx -> {
                var query = new Query(
                        "Match (n:Author) Where n.message = $message RETURN n.message + ', from node ' + id(n)",
                        parameters("message", "2")
                );
                var result = tx.run(query);
                return result.single().get(0).asString();
            });
            System.out.println(greeting);
        }
        return null;
    }
}
