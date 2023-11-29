package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.entity.dao.Author;
import com.dlut.ResearchService.mapper.AuthorMapper;
import com.dlut.ResearchService.service.IAuthorService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zsl
 * @since 2023-06-03
 */
@Service
public class AuthorServiceImpl implements IAuthorService {
    @Resource
    private ResultBuilder resultBuilder;
    @Resource
    private AuthorMapper authorMapper;

    @Override
    public Result getAuthorInfoById(Integer id) {
        Author authorInfo = authorMapper.selectAuthorInfoById(id);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "Success", "");
    }

    @Override
    public Result getCoAuthorRelatedGraph(Integer authorId) {
        return null;
    }

}

