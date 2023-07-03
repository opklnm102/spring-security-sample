package com.example.customuser2.extension;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(ComposeExtension.class)
@DirtiesContext
@ActiveProfiles("test")
public class UserControllerITests {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void login() {
        var userName = "bronze@example.com";
        var password = "1234";

        given()
                .auth()
                .basic(userName, password)
                .contentType(ContentType.JSON)
                .when()
                .get("/user")
                .then()
                .statusCode(200)
                .assertThat().body("username", equalTo(userName))
                .log().all();
    }
}
