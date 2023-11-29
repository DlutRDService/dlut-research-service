package com.dlut.ResearchService.component;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import jakarta.annotation.Resource;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component("neo4j")
public class Neo4j {
    @Resource
    private ResultBuilder resultBuilder;
    @Resource
    private Driver driver;

    public Result getCoAuthorIds(Integer author_id) {
        List<Integer> data = null;
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "Success", null);
    }

    public Result queryRelatedGraph(Integer id, String cypher) {
        HashMap<String, Integer> data = new HashMap<>();
        try (var session = driver.session()) {
            List<Record> records = session.executeRead(tx -> {
                var query = new Query(cypher);
                // TODO 搞一下返回的查询结果类型
                return tx.run(query).stream().toList();
            });
            for (Record record : records) {
                String coAuthor = record.get("coAuthor").asString();
                int coAuthorCount = record.get("coAuthorCount").asInt();
                data.put(coAuthor, coAuthorCount);
            }
            return resultBuilder.build(
                    StatusCode.STATUS_CODE_200,
                    "",
                    data);
        }
    }
}
