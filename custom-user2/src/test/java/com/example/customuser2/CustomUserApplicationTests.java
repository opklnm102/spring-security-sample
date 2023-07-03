package com.example.customuser2;

import com.example.customuser2.singleton.SecurityConfiguration;
import com.example.customuser2.web.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import({
        SecurityConfiguration.class
})
class CustomUserApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void userWhenNotAuthenticated() throws Exception {
        mvc.perform(get("/"))
           .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(email = "admin@example.com")
    void userWhenWithMockCustomUserThenOk() throws Exception {
        mvc.perform(get("/user"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.username", equalTo("admin@example.com")));
    }

    @Test
    @WithMockCustomUser(email = "admin@example.com")
    void userWhenWithMockCustomAdminThenOk() throws Exception {
        mvc.perform(get("/user"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.username", equalTo("admin@example.com")));
    }
}
