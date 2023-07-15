package com.example.academickg.service;

import com.example.academickg.entity.dao.EmailCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
public interface IEmailCodeService extends IService<EmailCode> {
    void sendEmailCode(String email, Integer type);
}
