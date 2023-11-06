package com.dlut.ResearchService.mapper;

import com.dlut.ResearchService.entity.dao.Paper;
import org.apache.ibatis.annotations.MapKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface PaperMapper {
    //批量插入
    Boolean saveBatch(List<Paper> paperList);
    //按id批量查询
    List<Paper> selectPaperByIds(List<Integer> idList);

    Paper selectPaperById(Integer id);

    List<Paper> selectPaperByIdList(List<Integer> idList);
    List<Paper> selectPaperByIdListPage(List<Integer> idList, Integer pageNum, Integer size);


    // 通过id删除

    Set<Integer> selectIds(String s);

    Set<Integer> selectIdsByAuthor(String query);

    Set<Integer> selectIdsByKeyword(String query);

    Set<Integer> selectIdsByPublishYear(int i);

    Set<Integer> selectIdByTitle(String subQuery);

    Set<Integer> selectIdsByJournal(String subQuery);

    Set<Integer> selectIdsByWOSCategory(String subQuery);
}
