package org.project.exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    private static final String SECURITY_SCHEME_NAME = "authorization"; // 추가

    @Bean
    public OpenAPI swaggerApi() {
        return new OpenAPI()
                .components(new Components()

                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(new Info()
                        .title("스프링시큐리티 + JWT 예제")
                        .description("스프링시큐리티와 JWT를 이용한 사용자 인증 예제입니다.")
                        .version("1.0.0"));
    }
}
