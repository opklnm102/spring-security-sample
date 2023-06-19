package com.example.oauth2jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
@Import(SecurityConfiguration.class)
public class HelloControllerTests {

    @Autowired
    MockMvc mvc;

    @Test
    void indexGreetsAuthenticatedUser() throws Exception {
        mvc.perform(get("/").with(jwt().jwt(jwt -> jwt.subject("ch4mpy"))))
                .andExpect(content().string(is("Hello, ch4mpy!")));
    }

    @Test
    void messageCanBeReadWithScopeMessageReadAuthority() throws Exception {
        mvc.perform(get("/message").with(jwt().jwt(jwt -> jwt.claim("scope", "message:read"))))
                .andExpect(content().string(is("secret message")));

        mvc.perform(get("/message").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_message:read"))))
                .andExpect(content().string(is("secret message")));
    }

    @Test
    void messageCanNotBeReadWithoutScopeMessageReadAuthority() throws Exception {
        mvc.perform(get("/message").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void messageCanNotBeCreatedWithoutAnyScope() throws Exception {
        mvc.perform(post("/message")
                        .content("Hello message")
                        .with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void messageCanNotBeCreatedWithScopeMessageReadAuthority() throws Exception {
        mvc.perform(post("/message")
                        .content("Hello message")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "message:read"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void messageCanBeCreatedWithScopeMessageWriteAuthority() throws Exception {
        mvc.perform(post("/message")
                .content("Hello message")
                .with(jwt().jwt(jwt -> jwt.claim("scope", "message:write"))))
                .andExpect(status().isOk())
                .andExpect(content().string(is("Message was created. Content: Hello message")));
    }
}
