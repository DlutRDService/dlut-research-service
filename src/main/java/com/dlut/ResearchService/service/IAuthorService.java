package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.dao.Author;

import java.util.List;

/**
 * @author zsl
 * @since 2023-06-03
 */
public interface IAuthorService {

    Result getAuthorInfoById(Integer id);

    Result getCoAuthorRelatedGraph(Integer authorId);
}
