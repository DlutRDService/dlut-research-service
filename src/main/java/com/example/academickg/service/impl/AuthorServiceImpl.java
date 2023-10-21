package com.example.academickg.service.impl;

import com.example.academickg.component.ResultBuilder;
import com.example.academickg.entity.constants.Result;
import com.example.academickg.entity.constants.StatusCode;
import com.example.academickg.entity.dto.AuthorDto;
import com.example.academickg.mapper.AuthorMapper;
import com.example.academickg.service.IAuthorService;
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
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "Sucess", data);
    }
}
