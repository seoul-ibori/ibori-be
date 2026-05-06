package com.springboot.iboribe.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    Server localServer = new Server().url("http://localhost:8080").description("Local Server");

    return new OpenAPI()
        .addServersItem(localServer)
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSecuritySchemes(
                    "accessTokenCookie",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE)
                        .name("ACCESS_TOKEN"))
                .addSecuritySchemes(
                    "refreshTokenCookie",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE)
                        .name("REFRESH_TOKEN")))
        .addSecurityItem(
            new SecurityRequirement()
                .addList("bearerAuth")
                .addList("accessTokenCookie")
                .addList("refreshTokenCookie"))
        .info(new Info().title("Ibori API 명세서").version("1.0").description("Seoul-Ibori API 문서"));
  }

  @Bean
  public GroupedOpenApi apiGroup() {
    return GroupedOpenApi.builder().group("api").pathsToMatch("/api/**").build();
  }
}
