package com.example.academickg.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingsDto implements Serializable {
    private final String registerMailTitle = "邮箱验证码";
    private final String registerEmailContent = "欢迎登陆知识图谱文献索引平台，您的邮箱验证码是: %s，5分钟内有效";
    private final Integer userInitSpace = 5;
}
