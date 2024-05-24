package com.example.oauthjwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Controller에서 보내주는 데이터 받을 수 있게
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**") //모든 경로에서
                .exposedHeaders("Set-Cookie") //노출 헤더 값
                .allowedOrigins("http://localhost:3000"); // front 서버 주소
    }
}
