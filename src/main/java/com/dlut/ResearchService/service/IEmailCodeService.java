package com.dlut.ResearchService.service;


import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsl
 * @since 2023-07-12
 */
public interface IEmailCodeService{
    @Transactional(rollbackFor = Exception.class)
    void sendEmailCode(String email);
}
