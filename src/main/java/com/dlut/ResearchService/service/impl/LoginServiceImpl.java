package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.entity.dao.UserInfo;
import com.dlut.ResearchService.mapper.UserInfoMapper;
import com.dlut.ResearchService.mapper.NoticeMapper;
import com.dlut.ResearchService.service.ILoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author zsl
 * @since 2023-07-12
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {
    private static final int SESSION_TIMEOUT_SECONDS = 1200;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private ResultBuilder resultBuilder;
    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 登录主页面
     */
    @Override
    public void login(@NotNull HttpServletResponse response, @NotNull HttpSession session) throws IOException {
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
        HashMap<String, Object> loginPageData = new HashMap<>();
        String notice = noticeMapper.selectPageNotice(0);
        // 直接隐藏验证码，在提交的时候再验证

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

    /**
     * 登陆，使用账号密码。管理员需要管理员身份验证
     * @param session 用户会话
     * @param password 用户密码
     * @return 用户存在且密码正确返回成功
     */
    @Override
    public Result signByAccount(@NotNull HttpSession session, String emailOrAccount, String password, String isManager) {
        if (isManager != null) {
            if (!userInfoMapper.checkManagerIdentity(emailOrAccount)){
                return resultBuilder.build(StatusCode.STATUS_CODE_500, "管理员身份错误");
            }
        }
        return checkLogin(session, emailOrAccount, password);
    }

    @Override
    public Result signByAccount(@NotNull HttpSession session, String emailOrAccount, String password) {
        return checkLogin(session, emailOrAccount, password);
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
        //Result result = verify()
        if (result.getData() == null){
            return result;
        } else if (result.getData() instanceof String) {
            if (userInfoMapper.checkStatusByEmail(email) == 0){
                return resultBuilder.build(StatusCode.STATUS_CODE_400, "登陆失败，用户已登录，若账号异常请修改密码");
            }
            session.setAttribute("email", email);
            String sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionID", sessionId);
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "登陆成功");
        }else {
            session.setAttribute("email", email);
            String sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionID", sessionId);
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "注册成功，请设置账号密码");
        }
    }

    /**
     * 注册用户设置账号密码
     * @param session 会话
     * @param account 账号
     * @param password 密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setPassword(@NotNull HttpSession session, Integer account, String password) {
        try{
            if (session.getAttribute("email") instanceof String email) {
                insert(password, email, account);
                return resultBuilder.build(StatusCode.STATUS_CODE_200, "设置成功");
            } else {
                return resultBuilder.build(StatusCode.STATUS_CODE_400, "设置失败");
            }
        } catch (Exception e){
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "设置失败");
        }
    }

    /**
     * 验证身份，可用于验证码登陆与密码信息修改
     * @param email 邮箱
     * @param emailCode 验证码
     */
    private Result verify(@NotNull String email, String emailCode){
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
    @Transactional(rollbackFor = Exception.class)
    public void insert(String password, String email, Integer account) {
        UserInfo userInfo = new UserInfo();
        try {
            File file = new File("../../../default_avatar.png");
            byte[] imageData = Files.readAllBytes(file.toPath());
            userInfo.setAvatar(imageData);
        } catch (IOException e) {
            log.error("初始头像加载错误");
            userInfo.setAvatar(null);
        }
        userInfo.setAccount(account);
        userInfo.setEmail(email);
        userInfo.setRegistrationTime(LocalDateTime.now());
        userInfo.setLastLoginTime(LocalDateTime.now());
        userInfo.setStatus(true);
        userInfo.setPassword(password);
        userInfoMapper.insert(userInfo);
        log.info("新用户：" + email + "，添加成功");
    }

    public Result checkLogin(HttpSession session, String emailOrAccount, String password){
        Integer userId = userInfoMapper.selectByEmailOrAccount(emailOrAccount);
        if (userId == null){
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "用户不存在，请检查输入的邮箱与密码或注册新用户");
        }
        if (userInfoMapper.checkStatusById(userId) == 0){
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "登陆失败，用户已登录，若账号异常请修改密码");
        }
        if (userInfoMapper.selectPasswordByEmailOrAccount(emailOrAccount, password) == null){
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "密码错误，请重新输入");
        }
        String sessionId = UUID.randomUUID().toString();
        session.setAttribute("sessionID", sessionId);
        session.setAttribute("email", emailOrAccount);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "登陆成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result recoveryPassword(HttpSession session, String email, String emailCode){
        return null;
    }

}
