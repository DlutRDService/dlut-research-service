package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.TreeNode;
import com.dlut.ResearchService.entity.dao.Paper;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


public interface IPaperService{
    Set<Integer> selectByQuery(String query);

    Result advancedQuery(String queryField);

    List<Paper> selectPapersByIdList(List<Integer> idList);

    Result paperInformation(Integer paperId);
}
