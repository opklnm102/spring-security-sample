package com.example.oauth2login;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest(classes = {Oauth2LoginApplication.class, OAuth2LoginApplicationTests.SecurityTestConfig.class})
@AutoConfigureMockMvc
public class OAuth2LoginApplicationTests {

    private static final String AUTHORIZATION_BASE_URI = "/oauth2/authorization";

    private static final String AUTHORIZE_BASE_URL = "http://localhost:8080/login/oauth2/code";

    @Autowired
    private WebClient webClient;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @BeforeEach
    void setUp() {
        webClient.getCookieManager().clearCookies();
    }

    @Test
    void requestIndexPageWhenNotAuthenticatedThenDisplayLoginPage() throws Exception {
        var page = webClient.<HtmlPage>getPage("/");
        assertLoginPage(page);
    }

    @Test
    void requestOtherPageWhenNotAuthenticatedThenDisplayLoginPage() throws Exception {
        var page = webClient.<HtmlPage>getPage("/other-page");
        assertLoginPage(page);
    }

    @Test
    void requestAuthorizeGitHubClientWhenLinkClickedThenStatusRedirectForAuthorization() throws Exception {
        var page = webClient.<HtmlPage>getPage("/");
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("github");

        var clientAnchorElement = getClientAnchorElement(page, clientRegistration);
        assertThat(clientAnchorElement).isNotNull();

        var response = followLinkDisableRedirects(clientAnchorElement);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value());

        var authorizeRedirectUri = response.getResponseHeaderValue("Location");
        assertThat(authorizeRedirectUri).isNotNull();

        var uriComponents = UriComponentsBuilder.fromUri(URI.create(authorizeRedirectUri)).build();
        var requestUri = uriComponents.getScheme() + "://" + uriComponents.getHost() + uriComponents.getPath();
        assertThat(requestUri).isEqualTo(clientRegistration.getProviderDetails().getAuthorizationUri());

        var params = uriComponents.getQueryParams().toSingleValueMap();
        assertThat(params.get(OAuth2ParameterNames.RESPONSE_TYPE)).isEqualTo(OAuth2AuthorizationResponseType.CODE.getValue());
        assertThat(params.get(OAuth2ParameterNames.CLIENT_ID)).isEqualTo(clientRegistration.getClientId());

        var redirectUri = AUTHORIZE_BASE_URL + "/" + clientRegistration.getRegistrationId();
        assertThat(URLDecoder.decode(params.get(OAuth2ParameterNames.REDIRECT_URI), StandardCharsets.UTF_8)).isEqualTo(redirectUri);
        assertThat(URLDecoder.decode(params.get(OAuth2ParameterNames.SCOPE), StandardCharsets.UTF_8)).isEqualTo(String.join(" ", clientRegistration.getScopes()));
        assertThat(params.get(OAuth2ParameterNames.STATE)).isNotNull();
    }

    @Test
    void requestAuthorizeClientWhenInvalidClientThenStatusInternalServerError() throws Exception {
        var page = webClient.<HtmlPage>getPage("/");
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("github");

        var clientAnchorElement = getClientAnchorElement(page, clientRegistration);
        assertThat(clientAnchorElement).isNotNull();

        clientAnchorElement.setAttribute("href", clientAnchorElement.getHrefAttribute() + "-invalid");
        WebResponse response = null;
        try {
            clientAnchorElement.click();
        } catch (FailingHttpStatusCodeException e) {
            response = e.getResponse();
        }

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void requestAuthorizationCodeGrantWhenValidAuthorizationResponseThendisplayIndexPage() throws Exception {
        var page = webClient.<HtmlPage>getPage("/");
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("github");

        var clientAnchorElement = getClientAnchorElement(page, clientRegistration);
        assertThat(clientAnchorElement).isNotNull();


        var response = followLinkDisableRedirects(clientAnchorElement);
        var authorizeRequestUriComponents = UriComponentsBuilder.fromUri(URI.create(response.getResponseHeaderValue("Location"))).build();

        var params = authorizeRequestUriComponents.getQueryParams().toSingleValueMap();
        var code = "auth-code";
        var state = URLDecoder.decode(params.get(OAuth2ParameterNames.STATE), StandardCharsets.UTF_8);
        var redirectUri = URLDecoder.decode(params.get(OAuth2ParameterNames.REDIRECT_URI), StandardCharsets.UTF_8);

        var authorizationResponseUri = UriComponentsBuilder.fromHttpUrl(redirectUri)
                .queryParam(OAuth2ParameterNames.CODE, code)
                .queryParam(OAuth2ParameterNames.STATE, state)
                .build()
                .encode().toUriString();

        assertIndexPage(webClient.getPage(new URL(authorizationResponseUri)));
    }

    @Test
    void requestAuthorizationCodeGrantWhenNoMatchingAuthorizationRequestThenDisplayLoginPageWithError()
            throws Exception {
        var page = webClient.<HtmlPage>getPage("/");
        var loginPageUrl = page.getBaseURL();
        var loginErrorPageUrl = new URL(loginPageUrl.toString() + "?error");
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("github");

        var code = "auth-code";
        var state = "state";
        var redirectUri = AUTHORIZE_BASE_URL + "/" + clientRegistration.getRegistrationId();
        var authorizationResponseUri = UriComponentsBuilder.fromHttpUrl(redirectUri)
                .queryParam(OAuth2ParameterNames.CODE, code).queryParam(OAuth2ParameterNames.STATE, state).build()
                .encode().toUriString();

        // Clear session cookie will ensure the 'session-saved'
        // Authorization Request (from previous request) is not found
        webClient.getCookieManager().clearCookies();

        page = webClient.getPage(new URL(authorizationResponseUri));
        Assertions.assertThat(page.getBaseURL()).isEqualTo(loginErrorPageUrl);

        HtmlElement errorElement = page.getBody().getFirstByXPath("div");
        Assertions.assertThat(errorElement).isNotNull();
        Assertions.assertThat(errorElement.asNormalizedText()).contains("authorization_request_not_found");
    }

    @Test
    void requestAuthorizationCodeGrantWhenInvalidStateParamThenDisplayLoginPageWithError() throws Exception {
        var page = webClient.<HtmlPage>getPage("/");
        var loginPageUrl = page.getBaseURL();
        var loginErrorPageUrl = new URL(loginPageUrl.toString() + "?error");
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("github");

        var clientAnchorElement = getClientAnchorElement(page, clientRegistration);
        Assertions.assertThat(clientAnchorElement).isNotNull();
        followLinkDisableRedirects(clientAnchorElement);

        var code = "auth-code";
        var state = "invalid-state";
        var redirectUri = AUTHORIZE_BASE_URL + "/" + clientRegistration.getRegistrationId();
        var authorizationResponseUri = UriComponentsBuilder.fromHttpUrl(redirectUri)
                .queryParam(OAuth2ParameterNames.CODE, code).queryParam(OAuth2ParameterNames.STATE, state).build()
                .encode().toUriString();

        page = webClient.getPage(new URL(authorizationResponseUri));
        Assertions.assertThat(page.getBaseURL()).isEqualTo(loginErrorPageUrl);

        HtmlElement errorElement = page.getBody().getFirstByXPath("div");
        Assertions.assertThat(errorElement).isNotNull();
        Assertions.assertThat(errorElement.asNormalizedText()).contains("authorization_request_not_found");
    }

    @Test
    void requestWhenMockOAuth2LoginThenIndex() throws Exception {
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("github");
        mvc.perform(get("/").with(oauth2Login().clientRegistration(clientRegistration)))
                .andExpect(model().attribute("userName", "user"))
                .andExpect(model().attribute("clientName", "GitHub"))
                .andExpect(model().attribute("userAttributes", Collections.singletonMap("sub", "user")));
    }

    private void assertLoginPage(HtmlPage page) {
        assertThat(page.getTitleText()).isEqualTo("Please sign in");

        int expectedClients = 2;

        var clientAnchorElements = page.getAnchors();
        assertThat(clientAnchorElements.size()).isEqualTo(expectedClients);

        var githubClientRegistration = clientRegistrationRepository.findByRegistrationId("github");
        var facebookClientRegistration = clientRegistrationRepository.findByRegistrationId("facebook");

        var baseAuthorizeUri = AUTHORIZATION_BASE_URI + "/";
        var githubClientAuthorizeUri = baseAuthorizeUri + githubClientRegistration.getRegistrationId();
        var facebookClientAuthorizeUri = baseAuthorizeUri + facebookClientRegistration.getRegistrationId();


        for (int i = 0; i < expectedClients; i++) {
            assertThat(clientAnchorElements.get(i).getAttribute("href")).isIn(githubClientAuthorizeUri,
                    facebookClientAuthorizeUri);
            assertThat(clientAnchorElements.get(i).asNormalizedText()).isIn(
                    githubClientRegistration.getClientName(),
                    facebookClientRegistration.getClientName()
            );
        }
    }

    private void assertIndexPage(HtmlPage page) {
        assertThat(page.getTitleText()).isEqualTo("Spring Security - OAuth 2.0 Login");

        var divElements = page.getBody().getElementsByTagName("div");
        Assertions.assertThat(divElements.get(4).asNormalizedText())
                .contains("You are successfully logged in joeg@springsecurity.io");
    }

    private HtmlAnchor getClientAnchorElement(HtmlPage page, ClientRegistration clientRegistration) {
        return page.getAnchors().stream()
                .filter(e -> e.asNormalizedText().equals(clientRegistration.getClientName()))
                .findFirst()
                .orElse(null);
    }

    private WebResponse followLinkDisableRedirects(HtmlAnchor anchor) throws Exception {
        WebResponse response = null;
        try {
            webClient.getOptions().setRedirectEnabled(false);
            anchor.click();
        } catch (FailingHttpStatusCodeException e) {
            response = e.getResponse();
            webClient.getOptions().setRedirectEnabled(true);
        }
        return response;
    }

    @Configuration
    @EnableWebSecurity
    public static class SecurityTestConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // @formatter:off
            return http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                       .oauth2Login(oauth2 -> oauth2.tokenEndpoint(token -> token.accessTokenResponseClient(mockAccessTokenResponseClient()))
                       .userInfoEndpoint(userInfo -> userInfo.userService(mockUserService())))
                       .build();
            // @formatter:on
        }

        private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> mockAccessTokenResponseClient() {
            OAuth2AccessTokenResponse accessTokenResponse = OAuth2AccessTokenResponse.withToken("access-token-1234")
                    .tokenType(OAuth2AccessToken.TokenType.BEARER).expiresIn(60 * 1000).build();

            var tokenResponseClient = mock(OAuth2AccessTokenResponseClient.class);
            when(tokenResponseClient.getTokenResponse(any())).thenReturn(accessTokenResponse);
            return tokenResponseClient;
        }

        private OAuth2UserService<OAuth2UserRequest, OAuth2User> mockUserService() {
            var attributes = new HashMap<String, Object>();
            attributes.put("id", "joeg");
            attributes.put("first-name", "Joe");
            attributes.put("last-name", "Grandja");
            attributes.put("email", "joeg@springsecurity.io");

            GrantedAuthority authority = new OAuth2UserAuthority(attributes);
            var authorities = new HashSet<GrantedAuthority>();
            authorities.add(authority);

            DefaultOAuth2User user = new DefaultOAuth2User(authorities, attributes, "email");

            var userService = mock(OAuth2UserService.class);
            when(userService.loadUser(any())).thenReturn(user);
            return userService;
        }

        @Bean
        OAuth2AuthorizedClientRepository authorizedClientRepository() {
            return new HttpSessionOAuth2AuthorizedClientRepository();
        }
    }
}
