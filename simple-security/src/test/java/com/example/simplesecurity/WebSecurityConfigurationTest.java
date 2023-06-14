package com.example.simplesecurity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@Import(WebSecurityConfiguration.class)
public class WebSecurityConfigurationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithAnonymousUser
    public void whenAnonymousAccessLogin_thenOk() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void whenAnonymousAccessRestrictedEndpoint_thenIsUnauthorized() throws Exception {
        mvc.perform(get("/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails()
    public void whenUserAccessUserSecuredEndpoint_thenOk() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails()
    public void whenUserAccessRestrictedEndpoint_thenOk() throws Exception {
        mvc.perform(get("/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails()
    public void whenUserAccessAdminSecuredEndpoint_thenIsForbidden() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails()
    public void whenUserAccessDeleteSecuredEndpoint_thenIsForbidden() throws Exception {
        mvc.perform(delete("/delete"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin")
    public void whenAdminAccessUserEndpoint_thenOk() throws Exception {
        mvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "admin")
    public void whenAdminAccessAdminSecuredEndpoint_thenIsOk() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "admin")
    public void whenAdminAccessDeleteSecuredEndpoint_thenIsOk() throws Exception {
        mvc.perform(delete("/delete").content("{}"))
                .andExpect(status().isOk());
    }
}
