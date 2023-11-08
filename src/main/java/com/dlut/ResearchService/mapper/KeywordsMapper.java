package com.dlut.ResearchService.mapper;

import com.dlut.ResearchService.entity.dao.Keywords;

import java.io.Serializable;

/**
 * @author zsl
 * @since 2023-06-03
 */
public interface KeywordsMapper {
    Keywords selectById(Serializable id);
}
