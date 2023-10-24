package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import jakarta.servlet.http.HttpSession;

/**
 * @author zsl
 * @since 2023-07-12
 */
public interface ILogService {

    Result signByAccount(HttpSession session, String email, String password);

    Result signByEmailCodeOrRegistration(HttpSession session, String email, String captcha);

    Result changePassword(HttpSession session, String newPassword);
}
