package com.dlut.ResearchService.service;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author zsl
 * @since 2023-07-12
 */
public interface IEmailCodeService{
    @Transactional(rollbackFor = Exception.class)
    void sendEmailCode(String email);

    void getCaptcha(@NotNull HttpServletResponse response, @NotNull HttpSession session) throws IOException;

    void checkCaptcha(@NotNull HttpSession session, @NotNull String captcha);
}
