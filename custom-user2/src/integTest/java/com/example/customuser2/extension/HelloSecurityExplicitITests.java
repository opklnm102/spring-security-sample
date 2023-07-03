package com.example.customuser2.extension;

import com.example.customuser2.web.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(ComposeExtension.class)
@DirtiesContext
@ActiveProfiles("test")
public class HelloSecurityExplicitITests {

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
