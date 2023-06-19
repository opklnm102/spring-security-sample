package com.example.jwtlogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class JWTLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(JWTLoginApplication.class, args);
    }

}
