package com.dlut.ResearchService.controller.userController;

import com.dlut.ResearchService.annotation.RequestRateLimit;
import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.service.impl.EmailCodeServiceImpl;
import com.dlut.ResearchService.service.impl.LoginServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import com.dlut.ResearchService.entity.constants.StatusCode;

import java.io.IOException;


@RestController
@RequestMapping("login")
public class LoginController {
    private static final int SESSION_TIMEOUT_SECONDS = 1200;
    @Resource
    private EmailCodeServiceImpl emailCodeService;
    @Resource
    private LoginServiceImpl loginService;
    @Resource
    private ResultBuilder resultBuilder;

    /**
     * 登陆，使用账号密码。
     * @param session 会话
     * @param emailOrAccount 邮箱或账号
     * @param password 密码
     * @param captcha 图片验证码
     */
    @RequestRateLimit
    @PostMapping("sign-in/account")
    public Result signByAccount(HttpSession session,
                                @RequestParam String emailOrAccount,
                                @RequestParam String password, @NotNull @RequestParam String captcha) {
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
        emailCodeService.checkCaptcha(session, captcha);
        return loginService.signByAccount(session, emailOrAccount, password);
    }

    /**
     * 登陆，验证码登陆。
     * @param email 邮箱
     * @param emailCode 邮箱验证码
     * @param captcha 图片验证码
     */
    @RequestRateLimit
    @PostMapping("sign-in/emailCode")
    public Result signByEmailCode(@NotNull HttpSession session, @RequestParam String email, @RequestParam String emailCode,
                                  @NotNull @RequestParam String captcha){
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
        emailCodeService.checkCaptcha(session, captcha);
        return loginService.signByEmailCodeOrRegistration(session, email, emailCode);
    }

    /**
     * 用户注册
     * @param email 注册邮箱
     * @param emailCode 邮箱验证码
     * @param captcha 图片验证码
     */
    @RequestRateLimit
    @PostMapping("sign-up/registration")
    public Result registration(HttpSession session, String email, String emailCode, String captcha){
        emailCodeService.checkCaptcha(session, captcha);
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
        return loginService.signByEmailCodeOrRegistration(session, email, emailCode);
    }

    /**
     * 设置密码，密码设置成功后修改或导入数据库
     * @param password 密码
     */
    @PostMapping("setAccountAndPassword")
    public Result setPassword(HttpSession session, @RequestParam String password, Integer account){
        return loginService.updatePassword(session, password, account);
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
     */
    @RequestRateLimit
    @PostMapping("sendEmailCode")
    public Result sendEmailCode(String email){
        emailCodeService.sendEmailCode(email);
        return resultBuilder.build(StatusCode.STATUS_CODE_200,"验证码已发送", null);
    }
}
