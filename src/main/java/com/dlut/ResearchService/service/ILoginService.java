package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;

/**
 * @author zsl
 * @since 2023-07-12
 */
public interface ILoginService {

    Result signByAccount(HttpSession session, String email, String password);

    Result signByEmailCodeOrRegistration(HttpSession session, String email, String captcha);

    Result updatePassword(@NotNull HttpSession session, String newPassword, Integer account);

    String selectPageNotice(Integer page);
}
