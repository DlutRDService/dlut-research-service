package com.dlut.ResearchService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("""
                -------------------
                (♥◠‿◠)ﾉﾞ  I Love DLUT   ლ(´ڡ`ლ)ﾞ
                ,------.  ,--.   ,--. ,--.,--------.
                |  .-.  \\ |  |   |  | |  |'--.  .--'
                |  |  \\  :|  |   |  | |  |   |  |
                |  '--'  /|  '--.'  '-'  '   |  |
                `-------' `-----' `-----'    `--'
                -------------------""");
        SpringApplication.run(Application.class, args);
    }
}
