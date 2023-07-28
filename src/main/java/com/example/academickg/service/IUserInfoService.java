package com.example.academickg.service;

import com.example.academickg.common.Result;
import com.example.academickg.entity.dao.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
public interface IUserInfoService {
    Result login(Integer account, String password);

    Result changePassword(Integer account, String password, String email);

    Result insert(Integer account, String password, String email);
}
