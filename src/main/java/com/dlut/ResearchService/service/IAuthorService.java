package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.dto.AuthorDto;

import java.util.List;

/**
 * @author zsl
 * @since 2023-06-03
 */
public interface IAuthorService {
    Result getAuthorInfo(AuthorDto author);
    Result insertOneRecord();
    Result insertRecords();
    Result selectById(Integer id);
    Result selectByIds(List<Integer> ids);
    Result selectByName(String name);
    Result selectByNames(List<String> names);
    Result getAuthorInfoById(Integer id);
    Result relatedPaperRecommendation(List<String> keywords);

    Result getCoAuthorRelatedGraph(Integer authorId);
}