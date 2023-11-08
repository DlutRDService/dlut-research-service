package com.dlut.ResearchService.mapper;

import com.dlut.ResearchService.entity.dao.Paper;

import java.util.List;
import java.util.Set;


public interface PaperMapper {
    Paper selectPaperById(Integer id);
    List<Paper> selectPaperByIdList(List<Integer> idList);
    List<Paper> selectPaperByIdListPage(List<Integer> idList, Integer pageNum, Integer size);
    Set<Integer> selectIds(String s);
    Set<Integer> selectIdsByAuthor(String query);
    Set<Integer> selectIdsByKeyword(String query);
    Set<Integer> selectIdsByPublishYear(int i);
    Set<Integer> selectIdByTitle(String subQuery);
    Set<Integer> selectIdsByJournal(String subQuery);
    Set<Integer> selectIdsByWOSCategory(String subQuery);
}
