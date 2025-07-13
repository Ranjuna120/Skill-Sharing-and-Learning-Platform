package com.example.skill_sharing_backend.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders(
                    "Authorization",
                    "Content-Type",
                    "X-Requested-With",
                    "Accept",
                    "Origin",
                    "Access-Control-Request-Method",
                    "Access-Control-Request-Headers"
                )
                .exposedHeaders(
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Credentials",
                    "Authorization",
                    "Content-Type"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }
}