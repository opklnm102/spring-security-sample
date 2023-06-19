package com.example.customuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.Map;

@SpringBootApplication
public class CustomUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomUserApplication.class, args);
    }

    @Bean
    public MapCustomUserRepository userRepository() {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var customUser = new CustomUser(1, "user@example.com", encoder.encode("password"));
        var emailToCustomUser = Map.of(customUser.getEmail(), customUser);
        return new MapCustomUserRepository(emailToCustomUser);
    }
}
