package com.example.academickg.service.impl;

import com.example.academickg.common.Result;
import com.example.academickg.entity.constants.Regex;
import com.example.academickg.entity.constants.StatusCode;
import com.example.academickg.entity.dao.UserInfo;
import com.example.academickg.mapper.EmailCodeMapper;
import com.example.academickg.mapper.UserInfoMapper;
import com.example.academickg.service.IUserInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

/**
 * @author zsl
 * @since 2023-07-12
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements IUserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private EmailCodeMapper emailCodeMapper;
    @Resource
    private Result result;
    private static final int SESSION_TIMEOUT_SECONDS = 1200;

    /**
     * 登陆，使用账号密码。
     * @param session 用户会话
     * @param password 用户密码
     * @return 用户存在且密码正确返回成功
     */
    @Override
    public Result signByAccount(HttpSession session, String email, String password) {
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
        Integer userId = userInfoMapper.selectByEmail(email);
        if (userId == null){
            return result.changeResultState(result, StatusCode.STATUS_CODE_400, "用户不存在，请注册或检查输入的邮箱与密码");
        }
        if (userInfoMapper.selectByEmailAndPassword(email, password) == null){
            return result.changeResultState(result, StatusCode.STATUS_CODE_400, "密码错误，请重新输入");
        }
        session.setAttribute("email", email);
        session.setAttribute("password", password);
        session.setAttribute("userId", userId);
        return result.changeResultState(result, StatusCode.STATUS_CODE_200, "登陆成功");
    }

    /**
     * 验证码登陆, 第一次登陆直接注册
     * @param session 会话
     * @param email 邮箱
     * @param checkCode 验证码
     */
    @Override
    public Result signByCaptchaOrRegistration(HttpSession session, String email, String checkCode) {
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
        result = verify(email, checkCode);
        if (result.getData() == null){
            return result;
        } else if (result.getData() instanceof String) {
            session.setAttribute("email", email);
            session.setAttribute("password", result.getData());
            return result.changeResultState(result, StatusCode.STATUS_CODE_200, "登陆成功");
        }else {
            session.setAttribute("email", email);
            session.setAttribute("password", null);
            return result.changeResultState(result,StatusCode.STATUS_CODE_200, "注册成功，请设置密码");
        }
    }
    @Override
    public Result changePassword(HttpSession session, String newPassword) {
        String email = (String) session.getAttribute("email");
        String oldPassword = (String) session.getAttribute("password");
        if (oldPassword == null){
            insert(newPassword, email);
            return result.changeResultState(result, StatusCode.STATUS_CODE_200, "密码设置成功");
        }
        if (userInfoMapper.updatePassword(oldPassword, email)){
            return result.changeResultState(result, StatusCode.STATUS_CODE_200, "密码修改成功");
        }
        return result.changeResultState(result, StatusCode.STATUS_CODE_400, "密码修改失败");
    }
    /**
     * 验证身份，可用于验证码登陆与密码信息修改
     * @param email 邮箱
     * @param checkCode 验证码
     */
    public Result verify(String email, String checkCode){
        if (!email.matches(Regex.DLUT_MAIL)){
            return result.changeResultState(
                    result, StatusCode.STATUS_CODE_400,
                    "请使用大工邮箱");
        }
        if (!emailCodeMapper.selectCodeByEmail(email).equals(checkCode)){
            return result.changeResultState(
                    result, StatusCode.STATUS_CODE_400,
                    "验证码输入错误，请重新输入");
        }
        if (userInfoMapper.isEmailExit(email)){
            return result.changeResultState(
                    result, StatusCode.STATUS_CODE_200,
                    "操作成功", userInfoMapper.selectPasswordByEmail(email));
        }else {
            return result.changeResultState(
                    result, StatusCode.STATUS_CODE_200,
                    "操作成功", false);
        }

    }

    /**
     * 新增用户
     * @param password 密码
     * @param email    邮箱
     */
    public void insert(String password, String email) {
        byte[] imageData = new byte[0];
        try {
            File file = new File("../../../default_avatar.png");
            imageData = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("初始头像加载错误");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setAvatar(imageData);
        userInfo.setRegistrationTime(LocalDateTime.now());
        userInfo.setLastLoginTime(LocalDateTime.now());
        userInfo.setStatus(true);
        userInfo.setPassword(password);
        userInfoMapper.insert(userInfo);
        log.info("新用户：" + email + "，添加成功");
    }

    /**
     * 修改用户信息
     * @param userId 用户Id
     * @param userInfo 用户信息列表
     */
    public Result modifyUseInfo(Integer userId, UserInfo userInfo){
        if (userId == null) {
            return result.changeResultState(result, StatusCode.STATUS_CODE_500, "拒绝访问");
        }
        if (userInfoMapper.update(userInfo)){
            return result.changeResultState(result, StatusCode.STATUS_CODE_200, "修改成功");
        }else{
            return result.changeResultState(result, StatusCode.STATUS_CODE_400, "修改失败");
        }
    }
}
