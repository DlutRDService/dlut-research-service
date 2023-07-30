package com.example.academickg.service.impl;

import com.example.academickg.common.Result;
import com.example.academickg.constants.EmailConstants;
import com.example.academickg.entity.dao.UserInfo;
import com.example.academickg.mapper.EmailCodeMapper;
import com.example.academickg.mapper.UserInfoMapper;
import com.example.academickg.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private EmailCodeMapper emailCodeMapper;

    @Override
    public Result login(Integer account, String password) {
        if (userInfoMapper.selectByAccount(account) == null){
            return Result.systemError("用户不存在，请先注册", null);
        }
        if (userInfoMapper.selectByAccountAndPassword(account, password) == null){
            return Result.systemError("密码错误，请重新输入", null);
        }
        return Result.success("登陆成功", null);
    }

    /**
     * 根据用户信息，修改密码更新数据库
     * @param account 账号
     * @param password 密码
     * @param email 邮箱
     */
    @Override
    public Result changePassword(Integer account, String password, String email) {
        if (userInfoMapper.updatePassword(account, password, email)){
            return Result.success("密码修改成功", true);
        }
        return Result.permissionsError("密码修改失败", null);
    }

    @Override
    public Result insert(Integer account, String password, String email) {
        return null;
    }

    public Result verify(Integer account, String email, String checkCode){
        if (userInfoMapper.selectByAccount(account) == 1){
            return Result.paramError("账户已存在，请勿重复注册，若忘记密码，可用邮箱进行找回", null);
        }
        if (userInfoMapper.selectByAccountAndEmail(account, email) == 1){
            return Result.paramError("邮箱已绑定其他账号，请检查您的输入信息", null);
        }
        if (!emailCodeMapper.selectCodeByEmail(email).equals(checkCode)){
            return Result.paramError("验证码错误，请重新输入", null);
        }
        return Result.success("操作成功，请设置密码", true);
    }

    /**
     * 导入数据库用户信息
     * @param account  账号
     * @param password 密码
     * @param email    邮箱
     */
    public void inputUserInfo(Integer account, String password, String email) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        userInfo.setLastLoginTime(LocalDateTime.now());
        userInfo.setRegistrationTime(LocalDateTime.now());
        userInfo.setStatus(EmailConstants.STATUS);
        userInfoMapper.insert(userInfo);
    }
}
