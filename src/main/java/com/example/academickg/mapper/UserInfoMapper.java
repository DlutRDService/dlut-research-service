package com.example.academickg.mapper;

import com.example.academickg.entity.dao.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    Integer selectByEmail(String email);

    Integer selectByAccount(Integer account);

    Integer selectByAccountAndPassword(Integer account, String password);

    Integer selectByAccountAndEmail(Integer account, String email);
    // Integer insert(UserInfo userInfo);
    Boolean updatePassword(Integer account, String password, String email);

}
