package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author zsl
 * @since 2023-07-12
 */
public interface ILoginService {

    void login(@NotNull HttpServletResponse response, @NotNull HttpSession session) throws IOException;

    Result signByAccount(@NotNull HttpSession session, String emailOrAccount, String password, String isManager);

    Result signByAccount(@NotNull HttpSession session, String emailOrAccount, String password);

    Result signByEmailCodeOrRegistration(HttpSession session, String email, String captcha);

    @Transactional(rollbackFor = Exception.class)
    Result setPassword(HttpSession session, Integer account, String password);

    @Transactional(rollbackFor = Exception.class)
    Result recoveryPassword(HttpSession session, String email, String emailCode);
}
