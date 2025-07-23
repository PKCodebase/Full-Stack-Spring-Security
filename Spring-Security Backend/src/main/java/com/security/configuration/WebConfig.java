package com.security.configuration;

// WebConfig.java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5174") // or 3000 based on React port
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
