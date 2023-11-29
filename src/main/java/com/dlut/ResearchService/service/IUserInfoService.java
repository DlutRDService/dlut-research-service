package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.dao.UserInfo;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

public interface IUserInfoService {
    @Transactional(rollbackFor = Exception.class)
    Result updatePassword(@NotNull HttpSession session, String newPassword, String oldPassword);

    @Transactional(rollbackFor = Exception.class)
    Result modifyUseInfo(Integer userId, UserInfo userInfo);
}
