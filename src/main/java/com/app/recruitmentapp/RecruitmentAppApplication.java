package com.app.recruitmentapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.app.recruitmentapp")
@EnableScheduling
public class RecruitmentAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruitmentAppApplication.class, args);
    }

}
