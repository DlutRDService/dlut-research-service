package com.example.academickg.service;

import com.example.academickg.common.Result;
import com.example.academickg.entity.dto.AuthorDto;

/**
 * @author zsl
 * @since 2023-06-03
 */
public interface IAuthorService {
    Result getAuthorInfo(AuthorDto author);
}
