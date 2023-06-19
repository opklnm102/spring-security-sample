package com.example.customuser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomUserApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void userWhenNotAuthenticated() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user@example.com")
    void userWhenWithUserDetailsThenOk() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    @WithUser
    void userWhenWithUserThenOk() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    @WithMockCustomUser(email = "admin@example.com")
    void userWhenWithMockCustomUserThenOk() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("admin@example.com")));
    }

    @Test
    @WithMockCustomUser(email = "admin@example.com")
    void userWhenWithMockCustomAdminThenOk() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("admin@example.com")));
    }
}
