package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.service.INeo4jService;
import jakarta.annotation.Resource;

import java.util.List;

public class Neo4jServiceImpl implements INeo4jService {
    @Resource
    private ResultBuilder resultBuilder;

    @Override
    public Result getCoAuthorIds(Integer author_id) {
        List<Integer> data = null;
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "Success", data);
    }

    @Override
    public Result queryRelatedGraph(Integer id) {
        return null;
    }

}
