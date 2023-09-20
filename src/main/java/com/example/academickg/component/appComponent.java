package com.example.academickg.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component("appConfig")
public class appComponent {
    @Value("${spring.mail.username:}")
    private String sendUserName;

}
