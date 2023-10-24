package com.dlut.ResearchService.controller;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.service.impl.EmailCodeServiceImpl;
import com.dlut.ResearchService.service.impl.LogServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import com.dlut.ResearchService.exception.BusinessException;
import com.dlut.ResearchService.annotation.VerifyParams;
import com.dlut.ResearchService.annotation.GlobalInterceptor;
import com.dlut.ResearchService.entity.constants.StatusCode;

import java.io.IOException;

import com.dlut.ResearchService.entity.constants.EmailConstants;

@RestController
@RequestMapping("login")
public class LoginController {

    @Resource
    private EmailCodeServiceImpl emailCodeService;
    @Resource
    private LogServiceImpl logService;
    @Resource
    private ResultBuilder resultBuilder;

    /**
     * 登陆，使用账号密码。
     * @param session 会话
     * @param emailOrAccount 邮箱或账号
     * @param password 密码
     */
    @GlobalInterceptor
    @PostMapping("sign-in/account")
    public Result signByAccount(HttpSession session, @RequestParam String emailOrAccount, @RequestParam String password) {
        return logService.signByAccount(session, emailOrAccount, password);
    }

    /**
     * 登陆，验证码登陆。
     * @param email 邮箱
     * @param emailCode 验证码
     */
    @PostMapping("sign-in/emailCode")
    public Result signByEmailCode(HttpSession session, @RequestParam String email, @RequestParam String emailCode){
        return logService.signByEmailCodeOrRegistration(session, email, emailCode);
    }

    /**
     * 用户注册
     * @param email 注册邮箱
     * @param emailCode 注册验证码
     */
    @GlobalInterceptor
    @PostMapping("sign-up")
    public Result registration(HttpSession session, String email, String emailCode){
        return logService.signByEmailCodeOrRegistration(session, email, emailCode);
    }

    /**
     * 设置密码，密码设置成功后修改或导入数据库
     * @param password 密码
     */
    @PostMapping("setPassword")
    public Result setPassword(HttpSession session, @RequestParam String password){
        return logService.changePassword(session, password);
    }
    /**
     * 生成验证码
     */
    @PostMapping("getCaptcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws
            IOException {
        emailCodeService.getCaptcha(response, session);
    }

    /**
     * 发送邮件验证码
     * @param email 目标邮箱
     * @param captcha 图片验证码
     */
    @GlobalInterceptor(checkParams = true)
    @PostMapping("/sendEmailCode")
    public Result sendEmailCode(HttpSession session, @VerifyParams(required = true) String email, String captcha){
        try {
            if (!captcha.equalsIgnoreCase((String) session.getAttribute(EmailConstants.CAPTCHA))){
                throw new BusinessException("图片验证码不正确，请刷新后重新输入");
            }
            emailCodeService.sendEmailCode(email);
            return resultBuilder.build(StatusCode.STATUS_CODE_200,"验证码已发送", null);
        } finally {
            session.removeAttribute(EmailConstants.CAPTCHA);
        }
    }

}
