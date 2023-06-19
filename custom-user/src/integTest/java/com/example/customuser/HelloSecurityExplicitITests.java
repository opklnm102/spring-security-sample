package com.example.customuser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloSecurityExplicitITests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void login() {
        var result = rest.withBasicAuth("user@example.com", "password")
                .getForObject("/user", CustomUser.class);

        assertThat(result.getEmail()).isEqualTo("user@example.com");
    }
}
