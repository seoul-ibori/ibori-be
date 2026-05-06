package com.springboot.iboribe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.springboot.iboribe.global.config.property.JwtProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(JwtProperties.class)
public class IboriBeApplication {

  public static void main(String[] args) {
    SpringApplication.run(IboriBeApplication.class, args);
  }
}
