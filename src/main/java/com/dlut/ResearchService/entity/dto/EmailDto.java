package com.dlut.ResearchService.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailDto {
    private final String registerMailTitle = "邮箱验证码";
    private final String registerEmailContent = "欢迎登陆大连理工大学科研服务平台，您的邮箱验证码是: %s，5分钟内有效";
    private final Integer userInitSpace = 5;
}
