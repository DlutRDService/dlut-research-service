package com.dlut.ResearchService.controller;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.dao.UserInfo;
import com.dlut.ResearchService.service.impl.LoginServiceImpl;
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
    private LoginServiceImpl logService;
    /**
     * 修改用户信息
     * @param userInfo 用户修改信息
     */
    @PostMapping("/modifyUserInfo")
    public Result modifyUseInfo(HttpSession session, @RequestBody UserInfo userInfo){
        return logService.modifyUseInfo((Integer) session.getAttribute("userId"), userInfo);
    }
    @PostMapping("/setting")
    public Result setting(){
        return null;
    }
}
