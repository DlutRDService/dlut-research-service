package com.example.academickg.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingsDto implements Serializable {
    private String registerMailTitle = "邮箱验证码";
    private String registerEmailContent = "欢迎登陆知识图谱文献索引平台，您的邮箱验证码是: %s，5分钟内有效";
    private Integer userInitSpace = 5;

    public String getRegisterMailTitle() {
        return registerMailTitle;
    }

    public void setRegisterMailTitle(String registerMailTitle) {
        this.registerMailTitle = registerMailTitle;
    }

    public String getRegisterEmailContent() {
        return registerEmailContent;
    }

    public void setRegisterEmailContent(String registerEmailContent) {
        this.registerEmailContent = registerEmailContent;
    }

    public Integer getUserInitSpace() {
        return userInitSpace;
    }

    public void setUserInitSpace(Integer userInitSpace) {
        this.userInitSpace = userInitSpace;
    }
}
