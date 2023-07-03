package com.example.customuser2.singleton;

import com.example.customuser2.web.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloSecurityExplicitITests extends ComposeContainer {

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
