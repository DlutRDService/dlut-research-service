package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;

public interface INeo4jService {
    Result getCoAuthorIds(Integer author_id);
    Result queryRelatedGraph(Integer id);
}
