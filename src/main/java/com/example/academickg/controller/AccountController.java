package com.example.academickg.controller;

import com.example.academickg.entity.constants.Result;
import com.example.academickg.entity.dao.UserInfo;
import com.example.academickg.service.impl.UserInfoServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
public class AccountController {
    @Resource
    private UserInfoServiceImpl userInfoService;
    /**
     * 修改用户信息
     * @param userInfo 用户修改信息
     */
    @PostMapping("/modifyUserInfo")
    public Result modifyUseInfo(HttpSession session, @RequestBody UserInfo userInfo){
        return userInfoService.modifyUseInfo((Integer) session.getAttribute("userId"), userInfo);
    }
    @PostMapping("/setting")
    public Result setting(){
        return null;
    }
}
