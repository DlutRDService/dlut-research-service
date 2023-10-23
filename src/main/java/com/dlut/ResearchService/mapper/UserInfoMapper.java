package com.dlut.ResearchService.mapper;

import com.dlut.ResearchService.entity.dao.UserInfo;

/**
 * @author zsl
 * @since 2023-07-12
 */
public interface UserInfoMapper{

    Integer selectByEmailAndPassword(String email, String password);

    // Integer insert(UserInfo userInfo);
    Boolean updatePassword(String password, String email);

    boolean isEmailExit(String email);

    Integer selectByEmail(String email);

    String selectPasswordByEmail(String email);
    Boolean insert(UserInfo userInfo);

    Boolean update(UserInfo userInfo);
}
