package com.example.academickg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableCaching
public class AcademicKgApplication {
    public static void main(String[] args) {
        SpringApplication.run(AcademicKgApplication.class, args);
    }

}
