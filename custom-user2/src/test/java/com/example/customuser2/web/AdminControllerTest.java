package com.example.customuser2.web;

import com.example.customuser2.WithMockCustomUser;
import com.example.customuser2.singleton.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.customuser2.configuration.ApiDocumentUtils.getDocumentRequest;
import static com.example.customuser2.configuration.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.example.com")
@Import({
        SecurityConfiguration.class
})
@ExtendWith(RestDocumentationExtension.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockCustomUser(email = "admin@example.com")
    void endpoint() throws Exception {
        // given

        // when
        ResultActions result = mvc.perform(get("/admin/{id}", 1L)
                .param("userName", "admin"));

        // then
        result.andExpect(status().isOk())
              .andExpect(content().string("admin"))
              .andDo(document("admin",
                      getDocumentRequest(),
                      getDocumentResponse()))
              .andDo(print());
    }
}
