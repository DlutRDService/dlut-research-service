package com.example.academickg.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component("appConfig")
public class AppConfig {
    @Value("${spring.mail.username:}")
    private String sendUserName;

}
