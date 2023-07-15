package com.example.academickg.mapper;

import com.example.academickg.entity.dao.Keywords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zsl
 * @since 2023-06-03
 */
public interface KeywordsMapper extends BaseMapper<Keywords> {
    @Override
    Keywords selectById(Serializable id);
}
