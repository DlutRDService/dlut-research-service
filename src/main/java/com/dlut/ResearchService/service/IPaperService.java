package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.dao.Paper;

import java.util.HashMap;
import java.util.List;


public interface IPaperService{
    HashMap<String, List<String>> queryFieldProcess(String queryField);
    List<Paper> queryProcess(HashMap<String, List<String>> hashMap);
}
