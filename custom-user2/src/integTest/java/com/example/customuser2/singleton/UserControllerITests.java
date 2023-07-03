package com.example.customuser2.singleton;

import com.example.customuser2.singleton.ComposeContainer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerITests extends ComposeContainer {

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
