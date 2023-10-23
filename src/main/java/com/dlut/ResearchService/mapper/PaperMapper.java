package com.dlut.ResearchService.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlut.ResearchService.entity.dto.PaperDto;
import com.dlut.ResearchService.entity.dao.Paper;
import org.apache.ibatis.annotations.MapKey;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface PaperMapper extends BaseMapper<Paper> {
    List<Integer> selectIds(String s);
    //批量插入
    Boolean saveBatch(List<Paper> paperList);
    //按id批量查询
    List<PaperDto> selectPaperByIds(List<Integer> idList);

    PaperDto selectPaperById(Integer id);

    List<PaperDto> selectPaperByIdList(List<Integer> idList);

    //Map<String, Object> selectKeywordsByPaperId(Integer id);

    //查询年份
    @MapKey("Year")
    List<Map<Object, Object>> selectYearList(List<Integer> idList);
    // 根据ID查询
    @Override
    Paper selectById(Serializable id);
    // 条件查询
    @Override
    default Paper selectOne(Wrapper<Paper> queryWrapper) {
        return BaseMapper.super.selectOne(queryWrapper);
    }
    //插入
    @Override
    int insert(Paper entity);
    // 通过id删除
    @Override
    int deleteById(Serializable id);
    // 通过columnMap删除
    @Override
    int deleteByMap(Map<String, Object> columnMap);
    // 根据实体条件删除
    @Override
    int delete(Wrapper<Paper> queryWrapper);
    // 通过批量id删除
    @Override
    int deleteBatchIds(Collection<?> idList);
    // 通过id修改
    @Override
    int updateById(Paper entity);
    // 通过条件更改
    @Override
    int update(Paper entity, Wrapper<Paper> updateWrapper);
    List<Paper> pageQuery(Integer current, Integer size);
    List<PaperDto> selectByYear(Integer Year);
    List<PaperDto> selectByAuthor(String Author);
    List<PaperDto> selectByWC(String WC);
    List<PaperDto> selectByESI(String ESI);
    List<PaperDto> selectByJournal(String Journal);

    List<Integer> selectIdByYear(Integer Year);
    List<Integer> selectIdByAuthor(String Author);
    List<Integer> selectIdByWC(String WC);
    List<Integer> selectIdByESI(String ESI);
    List<Integer> selectIdByJournal(String Journal);
    HashMap<Integer, String> selectTitleAndId();
}
