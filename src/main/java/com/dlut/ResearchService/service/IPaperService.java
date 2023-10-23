package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.dto.PaperDto;

import java.util.HashMap;
import java.util.List;


public interface IPaperService{
    HashMap<String, List<String>> queryFieldProcess(String queryField);
    List<PaperDto> queryProcess(HashMap<String, List<String>> hashMap);
}
