package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.TreeNode;
import com.dlut.ResearchService.entity.dao.Paper;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


public interface IPaperService{
    Set<Integer> selectByQuery(String query);


    List<Paper> selectPapersByIdList(List<Integer> ids);


    Result advancedResearch(HttpSession session, String queryField);


    Result advanceResearchByQueryList(HttpSession session, List<String> queries);

    Result paperInformation(Integer paperId);


    Result advancedQueryLimit(HttpSession session, Integer pageNum, Integer pageSize);
}
