package com.dlut.ResearchService.controller.userController;

import com.dlut.ResearchService.annotation.RequestRateLimit;
import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.service.impl.EmailCodeServiceImpl;
import com.dlut.ResearchService.service.impl.LoginServiceImpl;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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

    @RequestRateLimit
    @RequestMapping
    public void login(@NotNull HttpServletResponse response, @NotNull HttpSession session) throws IOException {
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
        HashMap<String, Object> loginPageData = new HashMap<>();
//        直接隐藏验证码得了，在提交的时候再验证
//        String captcha = emailCodeService.getCaptcha(response);
        String notice = loginService.selectPageNotice(0);

//        loginPageData.put("captcha", captcha);
        loginPageData.put("notice", notice);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(loginPageData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 将JSON字符串写入响应的输出流
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }

    @RequestRateLimit
    @PostMapping("sign-in/getCaptcha")
    public void signByAccount(HttpServletResponse response, HttpSession session,
                                @RequestParam @NotNull(value = "邮箱或账号不为空") String emailOrAccount,
                                @RequestParam @NotNull(value = "密码不为空") String password) throws IOException {
        emailCodeService.getCaptcha(response, session);
    }

    /**
     * 登陆，使用账号密码。
     * @param session 会话
     * @param emailOrAccount 邮箱或账号
     * @param password 密码
     * @param captcha 图片验证码
     */
    @RequestRateLimit
    @PostMapping("sign-in/account")
    public Result signByAccount(@NotNull HttpSession session,
                                @RequestParam String emailOrAccount,
                                @RequestParam String password, @NotNull @RequestParam String captcha) {
        emailCodeService.checkCaptcha(session, captcha);
        return loginService.signByAccount(session, emailOrAccount, password);
    }

    /**
     * 登陆，验证码登陆。
     * @param email 邮箱
     * @param emailCode 邮箱验证码
     */
    @RequestRateLimit
    @PostMapping("sign-in/emailCode")
    public Result signByEmailCode(@NotNull HttpSession session,
                                  @RequestParam String email,
                                  @RequestParam String emailCode){
        return loginService.signByEmailCodeOrRegistration(session, email, emailCode);
    }
    // TODO 注册完毕设置账号，密码
    /**
     * 用户注册
     * @param email 注册邮箱
     * @param emailCode 邮箱验证码
     */
    @RequestRateLimit
    @PostMapping("sign-up/registration")
    public Result registration(@NotNull HttpSession session,
                               @RequestParam String email,
                               @RequestParam String emailCode){
        return loginService.signByEmailCodeOrRegistration(session, email, emailCode);
    }
    // TODO 这里加一个新用户判断
    /**
     * 设置密码，密码设置成功后修改或导入数据库
     * @param password 密码
     */
    @RequestRateLimit
    @PostMapping("setAccountAndPassword")
    public Result setPassword(HttpSession session,
                              @RequestParam String password,
                              @RequestParam Integer account){
        return loginService.updatePassword(session, password, account);
    }

//     风险太大，考虑关闭
//    /**
//     * 生成验证码
//     */
//    @RequestRateLimit
//    @PostMapping("getCaptcha")
//    public void getCaptcha(HttpServletResponse response, HttpSession session) throws
//            IOException {
//        emailCodeService.getCaptcha(response, session);
//    }

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
