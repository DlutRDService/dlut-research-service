package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.entity.dao.UserInfo;
import com.dlut.ResearchService.mapper.UserInfoMapper;
import com.dlut.ResearchService.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private ResultBuilder resultBuilder;
    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 修改密码
     * @param session 会话
     * @param newPassword 新密码
     * @return 密码修改成功返回成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatePassword(@NotNull HttpSession session, String newPassword, String oldPassword) {
        if (session.getAttribute("email") instanceof Integer account){
            if (userInfoMapper.updatePasswordByAccount(account, newPassword, oldPassword)){
                return resultBuilder.build(StatusCode.STATUS_CODE_200, "密码修改成功");
            }
        } else if (session.getAttribute("email") instanceof String email){
            if (userInfoMapper.updatePasswordByEmail(email, newPassword, oldPassword)){
                return resultBuilder.build(StatusCode.STATUS_CODE_200, "密码修改成功");
            }
        }
        return resultBuilder.build(StatusCode.STATUS_CODE_400, "密码修改失败");
    }

    /**
     * 修改用户信息
     * @param userId 用户Id
     * @param userInfo 用户信息列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
