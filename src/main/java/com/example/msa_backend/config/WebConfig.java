package com.example.msa_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // TODO : 추후 수정
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .exposedHeaders("Authorization", "Set-Cookie");
    }
}
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // 모든 경로에 대해
//                .allowedOrigins("http://localhost:5173")  // Vite 프론트엔드 주소
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용 메서드
//                .allowedHeaders("*")  // 모든 헤더 허용
//                .allowCredentials(true);  // 인증 정보(Cookie 등) 포함 허용
//    }
//}