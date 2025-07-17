package com.security.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi(){

        return new OpenAPI()
                .info(new Info()
                        .title("Spring Security")
                        .description("Api for managing Spring Security")
                        .version("1.0.0"));
    }

}
