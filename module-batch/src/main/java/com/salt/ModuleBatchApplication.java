package com.salt;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class ModuleBatchApplication {
    public static void main(String[] args){
        SpringApplication.run(ModuleBatchApplication.class, args);
    }
}

// @EnableBatchProcessing
// 선언을 통해 배치 어플리케이션 구동에 필요한 설정을 자동으로 등록시켜줍니다.
