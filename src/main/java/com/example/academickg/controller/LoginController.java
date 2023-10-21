package com.example.academickg.controller;

import com.example.academickg.component.ResultBuilder;
import com.example.academickg.entity.constants.Result;
import com.example.academickg.service.impl.EmailCodeServiceImpl;
import com.example.academickg.service.impl.UserInfoServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import com.example.academickg.exception.BusinessException;
import com.example.academickg.annotation.VerifyParams;
import com.example.academickg.annotation.GlobalInterceptor;
import com.example.academickg.entity.constants.StatusCode;

import java.io.IOException;

import com.example.academickg.entity.constants.EmailConstants;

@RestController
@RequestMapping("login")
public class LoginController {

    @Resource
    private EmailCodeServiceImpl emailCodeService;
    @Resource
    private UserInfoServiceImpl userInfoService;
    @Resource
    private ResultBuilder resultBuilder;

    /**
     * 登陆，使用账号密码。
     * @param session 会话
     * @param email 邮箱
     * @param password 密码
     */
    @GlobalInterceptor
    @PostMapping("sign-in/account")
    public Result signByAccount(HttpSession session, @RequestParam String email, @RequestParam String password) {
        return userInfoService.signByAccount(session, email, password);
    }

    /**
     * 登陆，验证码登陆。
     * @param email 邮箱
     * @param checkCode 验证码
     */
    @PostMapping("sign-in/captcha")
    public Result signByCaptcha(HttpSession session, @RequestParam String email, @RequestParam String checkCode){
        return userInfoService.signByCaptchaOrRegistration(session, email, checkCode);
    }

    /**
     * 用户注册
     * @param email 注册邮箱
     * @param checkCode 注册验证码
     */
    @GlobalInterceptor
    @PostMapping("sign-up")
    public Result registration(HttpSession session, String email, String checkCode){
        return userInfoService.signByCaptchaOrRegistration(session, email, checkCode);
    }

    /**
     * 设置密码，密码设置成功后修改或导入数据库
     * @param password 密码
     */
    @PostMapping("setPassword")
    public Result setPassword(HttpSession session, @RequestParam String password){
        return userInfoService.changePassword(session, password);
    }
    /**
     * 生成验证码
     * @param type 0:登陆用验证码 1:邮箱发送用验证码
     */
    @PostMapping("getCaptcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session, Integer type) throws
            IOException {
        emailCodeService.getCaptcha(response, session, type);
    }

    /**
     * 发送邮件验证码
     * @param email 目标邮箱
     * @param checkCode 验证码
     * @param type 0：登陆用验证码 1：邮箱验证码
     */
    @GlobalInterceptor(checkParams = true)
    @PostMapping("/sendEmailCode")
    public Result sendEmailCode(HttpSession session, @VerifyParams(required = true) String email, String checkCode, Integer type){
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(EmailConstants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码不正确");
            }
            emailCodeService.sendEmailCode(email, type);
            return resultBuilder.build(StatusCode.STATUS_CODE_200,"验证码已发送", null);
        } finally {
            session.removeAttribute(EmailConstants.CHECK_CODE_KEY_EMAIL);
        }
    }

}
