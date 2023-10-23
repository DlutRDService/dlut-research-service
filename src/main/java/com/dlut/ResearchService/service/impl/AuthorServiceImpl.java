package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.entity.dto.AuthorDto;
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
    @Resource
    private MilvusServiceImpl milvusService;

    @Override
    public Result getAuthorInfo(AuthorDto author) {
        return null;
    }

    @Override
    public Result insertOneRecord() {
        return null;
    }

    @Override
    public Result insertRecords() {
        return null;
    }

    @Override
    public Result selectById(Integer id) {
        return null;
    }

    @Override
    public Result selectByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public Result selectByName(String name) {
        return null;
    }

    @Override
    public Result selectByNames(List<String> names) {
        return null;
    }

    @Override
    public Result getAuthorInfoById(Integer id) {
        AuthorDto data = authorMapper.selectAuthorInfoById(id);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "Success", data);
    }
    @Override
    public Result relatedPaperRecommendation(List<String> keywords) {
        // milvusService.searchRelatedPaper();
        return null;
    }

    @Override
    public Result getCoAuthorRelatedGraph(Integer authorId) {
        return null;
    }

}
