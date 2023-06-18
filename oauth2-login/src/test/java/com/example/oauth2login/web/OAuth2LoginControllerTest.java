package com.example.oauth2login.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest
class OAuth2LoginControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ClientRegistrationRepository clientRegistrationRepository;

    @Test
    void rootWhenAuthenticatedReturnsUserAndClient() throws Exception {
        mvc.perform(get("/").with(oauth2Login()))
                .andExpect(model().attribute("userName", "user"))
                .andExpect(model().attribute("clientName", "test"))
                .andExpect(model().attribute("userAttributes", Collections.singletonMap("sub", "user")));

    }

    @Test
    void rootWhenOverridingClientRegistrationReturnsAccordingly() throws Exception {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("test")
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .clientId("my-client-id")
                .clientName("my-client-name")
                .tokenUri("https://token-uri.example.org")
                .build();

        mvc.perform(get("/").with(oauth2Login()
                        .clientRegistration(clientRegistration)
                        .attributes(a -> a.put("sub", "spring-security"))))
                .andExpect(model().attribute("userName", "spring-security"))
                .andExpect(model().attribute("clientName", "my-client-name"))
                .andExpect(model().attribute("userAttributes", Collections.singletonMap("sub", "spring-security")));
    }

    @TestConfiguration
    static class AuthorizedClient {

        @Bean
        OAuth2AuthorizedClientRepository authorizedClientRepository() {
            return new HttpSessionOAuth2AuthorizedClientRepository();
        }

    }
}
