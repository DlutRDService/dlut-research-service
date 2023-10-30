package com.dlut.ResearchService.mapper;

import com.dlut.ResearchService.entity.dto.AuthorDto;

import java.util.List;

/**
 * @author zsl
 * @since 2023-06-03
 */
public interface AuthorMapper {
    AuthorDto selectAuthorInfoById(Integer authorId);
    List<String> getCoAuthor(List<Integer> authorIds);

}
