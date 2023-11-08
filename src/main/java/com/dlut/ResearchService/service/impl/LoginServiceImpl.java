package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.entity.dao.UserInfo;
import com.dlut.ResearchService.mapper.UserInfoMapper;
import com.dlut.ResearchService.service.ILoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author zsl
 * @since 2023-07-12
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private ResultBuilder resultBuilder;


    /**
     * 登陆，使用账号密码。
     * @param session 用户会话
     * @param password 用户密码
     * @return 用户存在且密码正确返回成功
     */
    @Override
    public Result signByAccount(@NotNull HttpSession session, String email, String password) {
        Integer userId = userInfoMapper.selectByEmail(email);
        if (userId == null){
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "用户不存在，请检查输入的邮箱与密码或注册新用户");
        }
        if (userInfoMapper.checkStatusById(userId) == 0){
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "登陆失败，用户已登录，若账号异常请修改密码");
        }
        if (userInfoMapper.selectByEmailAndPassword(email, password) == null){
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "密码错误，请重新输入");
        }
        String sessionId = UUID.randomUUID().toString();
        session.setAttribute("sessionID", sessionId);
        session.setAttribute("email", email);
        session.setAttribute("password", password);
        session.setAttribute("userId", userId);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "登陆成功");
    }

    /**
     * 验证码登陆, 第一次登陆直接注册
     * @param session 会话
     * @param email 邮箱
     * @param emailCode 验证码
     */
    @Override
    public Result signByEmailCodeOrRegistration(@NotNull HttpSession session, String email, String emailCode) {
        Result result  = verify(email, emailCode);
        if (result.getData() == null){
            return result;
        } else if (result.getData() instanceof String) {
            if (userInfoMapper.checkStatusByEmail(email) == 0){
                return resultBuilder.build(StatusCode.STATUS_CODE_400, "登陆失败，用户已登录，若账号异常请修改密码");
            }
            session.setAttribute("email", email);
            session.setAttribute("password", result.getData());
            String sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionID", sessionId);
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "登陆成功");
        }else {
            session.setAttribute("email", email);
            session.setAttribute("password", null);
            String sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionID", sessionId);
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "注册成功，请设置密码");
        }
    }

    /**
     * 修改密码，如果新注册用户则是设置密码
     * @param session 会话
     * @param newPassword 新密码
     * @param account 账号
     * @return 密码修改成功返回成功
     */
    @Override
    public Result updatePassword(@NotNull HttpSession session, String newPassword, Integer account) {
        String email = (String) session.getAttribute("email");
        String oldPassword = (String) session.getAttribute("password");
        if (oldPassword == null){
            insert(newPassword, email, account);
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "密码设置成功");
        }
        if (userInfoMapper.updatePassword(oldPassword, email)){
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "密码修改成功");
        }
        return resultBuilder.build(StatusCode.STATUS_CODE_400, "密码修改失败");
    }
    /**
     * 验证身份，可用于验证码登陆与密码信息修改
     * @param email 邮箱
     * @param emailCode 验证码
     */
    public Result verify(@NotNull String email, String emailCode){
        if (!email.matches(Regex.DLUT_MAIL)){
            return resultBuilder.build(
                    StatusCode.STATUS_CODE_400,
                    "请使用大工邮箱");
        }
        if (!redisService.get(email).equals(emailCode)){
            return resultBuilder.build(
                    StatusCode.STATUS_CODE_400,
                    "验证码输入错误，请重新输入");
        }
        if (userInfoMapper.isEmailExit(email) != null){
            return resultBuilder.build(
                    StatusCode.STATUS_CODE_200,
                    "操作成功", userInfoMapper.selectPasswordByEmail(email));
        }else {
            return resultBuilder.build(
                    StatusCode.STATUS_CODE_200,
                    "操作成功", false);
        }
    }

    /**
     * 新增用户
     * @param password 密码
     * @param email    邮箱
     */
    public void insert(String password, String email, Integer account) {
        byte[] imageData = new byte[0];
        try {
            File file = new File("../../../default_avatar.png");
            imageData = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("初始头像加载错误");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
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
            return resultBuilder.build(StatusCode.STATUS_CODE_500, "拒绝访问");
        }
        if (userInfoMapper.update(userInfo)){
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "修改成功");
        }else{
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "修改失败");
        }
    }
}
