package com.example.academickg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AcademicKgApplication {
    public static void main(String[] args) {
        SpringApplication.run(AcademicKgApplication.class, args);
    }


}
