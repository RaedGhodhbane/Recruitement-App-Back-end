package com.app.recruitmentapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.app.recruitmentapp")
public class RecruitmentAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruitmentAppApplication.class, args);
    }

}
