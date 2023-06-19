package com.example.simplesecurityweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SimpleSecurityWebApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void loginWithValidUserThenAuthenticated() throws Exception {
        var login = formLogin()
                .user("user")
                .password("password");

        mvc.perform(login)
                .andExpect(authenticated().withUsername("user"));
    }

    @Test
    void loginWithInvalidUserThenUnauthenticated() throws Exception {
        var login = formLogin()
                .user("invalid")
                .password("invalidpassword");

        mvc.perform(login)
                .andExpect(unauthenticated());
    }

    @Test
    void accessUnsecuredResourceThenOk() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void accessSecuredResourceUnauthenticatedThenRedirectsToLogin() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void accessSecuredResourceAuthenticatedThenOk() throws Exception {
        var mvcResult = mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).contains("Hello user!");
    }
}
