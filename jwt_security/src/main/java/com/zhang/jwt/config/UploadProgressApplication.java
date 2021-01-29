package com.zhang.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;

@Configuration
public class UploadProgressApplication {
   // 将 multipartResolver 指向我们刚刚创建好的继承 CommonsMultipartResolver 类的 自定义文件上传处理类
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CustomMultipartResolver customMultipartResolver = new CustomMultipartResolver();
        return customMultipartResolver;
    }
}