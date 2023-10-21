package com.example.academickg.service;

import com.example.academickg.entity.constants.Result;
import jakarta.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
public interface IUserInfoService {

    Result signByAccount(HttpSession session, String email, String password);

    Result signByCaptchaOrRegistration(HttpSession session, String email, String checkCode);

    Result changePassword(HttpSession session, String newPassword);
}
