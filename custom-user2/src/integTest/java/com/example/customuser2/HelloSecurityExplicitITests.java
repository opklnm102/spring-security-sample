package com.example.customuser2;

import com.example.customuser2.web.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class HelloSecurityExplicitITests {

    @Container
    private static final DockerComposeContainer<?> CONTAINERS = new DockerComposeContainer<>(new File("docker-compose-test.yml"))
            .withExposedService("mysql_1", 3306, Wait.forListeningPort());

    @DynamicPropertySource
    public static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_HOST", () -> CONTAINERS.getServiceHost("mysql_1", 3306));
        registry.add("DB_PORT", () -> CONTAINERS.getServicePort("mysql_1", 3306));
    }

    @Autowired
    private TestRestTemplate rest;

    @Test
    void login() {
        // given
        var userName = "bronze@example.com";
        var password = "1234";

        // when
        var result = rest.withBasicAuth(userName, password)
                         .getForObject("/user", UserDto.class);

        // then
        assertThat(result.username()).isEqualTo(userName);
    }
}
