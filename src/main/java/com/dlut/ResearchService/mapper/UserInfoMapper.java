package com.dlut.ResearchService.mapper;

import com.dlut.ResearchService.entity.dao.UserInfo;

/**
 * @author zsl
 * @since 2023-07-12
 */
public interface UserInfoMapper{

    Integer selectPasswordByEmailOrAccount(String email, String password);

    Boolean updatePasswordByAccount(Integer account, String newPassword, String oldPassword);
    Boolean updatePasswordByEmail(String email, String newPassword, String oldPassword);
    Boolean isEmailExit(String email);

    Integer selectByEmailOrAccount(String emailOrAccount);

    String selectPasswordByEmail(String email);
    Boolean insert(UserInfo userInfo);

    Boolean update(UserInfo userInfo);

    Integer checkStatusById(Integer userId);

    Integer checkStatusByEmail(String email);

    Boolean checkManagerIdentity(String emailOrAccount);
}
