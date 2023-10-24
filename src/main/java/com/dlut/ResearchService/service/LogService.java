package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import jakarta.servlet.http.HttpSession;

/**
 * @author zsl
 * @since 2023-07-12
 */
public interface LogService {

    Result signByAccount(HttpSession session, String email, String password);

    Result signByCaptchaOrRegistration(HttpSession session, String email, String checkCode);

    Result changePassword(HttpSession session, String newPassword);
}
