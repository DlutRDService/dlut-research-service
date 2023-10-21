package com.example.academickg.mapper;

import com.example.academickg.entity.dto.AuthorDto;

/**
 * @author zsl
 * @since 2023-06-03
 */
public interface AuthorMapper {
    AuthorDto selectAuthorInfoById(Integer id);
}
