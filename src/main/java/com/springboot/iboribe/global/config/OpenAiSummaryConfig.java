package com.springboot.iboribe.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiSummaryConfig {

  @Value("${openai.summary-api-key}")
  private String apiKey;

  @Bean
  public WebClient openAiSummaryWebClient() {
    return WebClient.builder()
        .baseUrl("https://api.openai.com/v1")
        .defaultHeader("Authorization", "Bearer " + apiKey)
        .build();
  }
}
