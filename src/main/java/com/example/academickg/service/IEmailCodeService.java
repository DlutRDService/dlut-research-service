package com.example.academickg.service;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
public interface IEmailCodeService{
    void sendEmailCode(String email, Integer type);
}
