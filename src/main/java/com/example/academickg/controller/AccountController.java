package com.example.academickg.controller;

import com.example.academickg.common.Result;
import com.example.academickg.service.impl.EmailCodeServiceImpl;
import com.example.academickg.service.impl.UserInfoServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import com.example.academickg.exception.BusinessException;

import java.io.IOException;

import com.example.academickg.entity.constants.EmailConstants;
import com.example.academickg.utils.CreateImageCode;

@RestController
@RequestMapping("login")
public class AccountController {

    @Resource
    private EmailCodeServiceImpl emailCodeService;
    @Resource
    private UserInfoServiceImpl userInfoService;
    private Result result;

    /**
     * 登陆，使用账号验证码或账号密码。
     */
    @GlobalInterceptor
    @PostMapping("sign-in")
    public Result login(HttpSession session, @RequestParam Integer account, @RequestParam String password) {
        session.setAttribute("account", account);
        session.setAttribute("password", password);
        return userInfoService.login(account, password);
    }

    /**
     * 用户注册或用户验证
     * @param account 注册账号
     * @param email 注册邮箱
     * @param checkCode 注册验证码
     */
    @PostMapping("registration")
    public Result registration(HttpSession session, Integer account, String email, String checkCode){
        result = userInfoService.verify(account, email, checkCode);
        if (result.getData() == null) return result;
        session.setAttribute("account", account);
        session.setAttribute("email", email);
        return result;
    }

    /**
     * 身份验证，用于修改密码时验证用户。(逻辑跟注册一样，可删除)
     * @param account 账号
     * @param email 邮箱
     * @param checkCode 验证码
     */
    @PostMapping("verify")
    public Result changePassword(HttpSession session, Integer account, String email, String checkCode){
        result = userInfoService.verify(account, email, checkCode);
        if (result.getData() == null){
            return result;
        }
        session.setAttribute("account", account);
        session.setAttribute("email", email);
        return result;
    }

    /**
     * 设置密码，密码设置成功后修改或导入数据库
     * @param password 密码
     */
    @PostMapping("setPassword")
    public Result setPassword(HttpSession session, String password){
        result = userInfoService.changePassword(
                (Integer) session.getAttribute("account"),
                password,
                (String) session.getAttribute("email")
        );
        if (result.getData() == null){
            userInfoService.inputUserInfo((Integer) session.getAttribute("account"),
                    password,
                    (String) session.getAttribute("email"));
            return new Result(StatusCode.STATUS_CODE_200, "密码设置成功", null);
        }
        session.setAttribute("password", password);
        return result;
    }
    /**
     * 生成验证码
     * @param type 0:登陆用验证码 1:邮箱发送用验证码
     */
    @PostMapping("getCaptcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session, Integer type) throws
            IOException {
        CreateImageCode vCode = new CreateImageCode(130,38,5,10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        if (type == null || type == 0){
            session.setAttribute(EmailConstants.CHECK_CODE_KEY, code);
        } else {
            session.setAttribute(EmailConstants.CHECK_CODE_KEY_EMAIL, code);
        }
        // System.out.println((String) session.getAttribute(Constants.CHECK_CODE_KEY));
        vCode.write(response.getOutputStream());
    }

    /**
     * 发送邮件验证码
     * @param email 目标邮箱
     * @param checkCode 验证码
     * @param type 0：登陆用验证码 1：邮箱验证码
     */
    @GlobalInterceptor(checkParams = true)
    @RequestMapping("/sendEmailCode")
    public Result sendEmailCode(HttpSession session, @VerifyParams(required = true) String email, String checkCode, Integer type){
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(EmailConstants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码不正确");
            }
            emailCodeService.sendEmailCode(email, type);
            return new Result(StatusCode.STATUS_CODE_200,"验证码已发送", null);
        } finally {
            session.removeAttribute(EmailConstants.CHECK_CODE_KEY_EMAIL);
        }
    }

}
