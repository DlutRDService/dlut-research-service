package com.dlut.ResearchService.controller.userController;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.dao.UserInfo;
import com.dlut.ResearchService.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
public class AccountController {
    @Resource
    private UserServiceImpl userService;
    /**
     * 修改用户信息
     * @param userInfo 用户修改信息
     */
    @PostMapping("modifyUserInfo")
    public Result modifyUseInfo(@NotNull HttpSession session, @RequestBody UserInfo userInfo){
        return userService.modifyUseInfo((Integer) session.getAttribute("userId"), userInfo);
    }
    @PostMapping("updatePassword")
    public Result updatePassword(@NotNull HttpSession session, String newPassword, String oldPassword){
        return userService.updatePassword(session, newPassword, oldPassword);
    }
}
