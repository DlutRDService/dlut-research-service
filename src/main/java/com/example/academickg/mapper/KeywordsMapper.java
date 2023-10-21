package com.example.academickg.mapper;

import com.example.academickg.entity.dao.Keywords;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zsl
 * @since 2023-06-03
 */
public interface KeywordsMapper {
    Keywords selectById(Serializable id);
}
