package com.example.academickg.mapper;

import com.example.academickg.entity.dao.EmailCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
public interface EmailCodeMapper extends BaseMapper<EmailCode> {
    void disableEmailCode(@Param("email") String email);
    int insert(@Param("emailCode") EmailCode emailCode);
    String selectCodeByEmail(String email);
}
