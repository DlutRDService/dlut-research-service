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

    //Map<String, Object> selectKeywordsByPaperId(Integer id);
    //查询年份
    @MapKey("Year")
    List<Map<Object, Object>> selectYearList(List<Integer> idList);

    // 通过id删除
    List<Paper> pageQuery(Integer current, Integer size);
    List<Paper> selectByYear(Integer Year);
    List<Paper> selectByAuthor(String Author);
    List<Paper> selectByWC(String WC);
    List<Paper> selectByESI(String ESI);
    List<Paper> selectByJournal(String Journal);
    List<Integer> selectIdByYear(Integer Year);
    List<Integer> selectIdByAuthor(String Author);
    List<Integer> selectIdByWC(String WC);
    List<Integer> selectIdByESI(String ESI);
    List<Integer> selectIdByJournal(String Journal);
    HashMap<Integer, String> selectTitleAndId();
    Object select(String s);
    Set<Integer> selectIds(String s);
}
