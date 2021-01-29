package com.zhang.jwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhang.mapper")
public class POIApplication {
    public static void main(String[] args) {
        SpringApplication.run(POIApplication.class, args);
    }
}
