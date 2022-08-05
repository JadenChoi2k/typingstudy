package com.typingstudy.interfaces.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.ResponseBodySnippet;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private String authToken;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    private String obtainAccessToken(String email, String password) throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        ResultActions result = mockMvc.perform(
                        post("/login")
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.ALL)
                )
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
        return result.andReturn().getResponse().getHeader("Authorization");
    }

    private String updateAccessToken() throws Exception {
        if (authToken == null) {
            this.authToken = obtainAccessToken("user1234@gmail.com", "1q2w3e4r");
        }
        return this.authToken;
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("/api/v1/user/join")
    void domain_join() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("email", "user1234@gmail.com");
        body.put("username", "user1234");
        body.put("password", "1q2w3e4r");
        body.put("profileUrl", "https://www.mypro.com/user1234");
        this.mockMvc.perform(
                        post("/api/v1/user/join")
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("domain-user-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("result")
                                        .description("result of join process"),
                                PayloadDocumentation.fieldWithPath("data.id")
                                        .description("id of user"),
                                PayloadDocumentation.fieldWithPath("data.username")
                                        .description("name of user"),
                                PayloadDocumentation.fieldWithPath("data.profileUrl")
                                        .description("profile url of user")
                        )
                ));
    }

    @Test
    @Order(2)
    @DisplayName("/login")
    void login() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("email", "user1234@gmail.com");
        body.put("password", "1q2w3e4r");
        ResultActions result = this.mockMvc.perform(
                        post("/login")
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.ALL)
                )
                .andExpect(status().isOk())
                .andDo(document("domain-user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(header().exists("Authorization"));
        this.authToken = result.andReturn().getResponse().getHeader("Authorization");
    }

    @Test
    @Order(3)
    @DisplayName("/api/v1/user/me")
    void me() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/user/me")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", updateAccessToken())
        )
                .andExpect(status().isOk())
                .andDo(document("user info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("result")
                                        .description("result"),
                                PayloadDocumentation.fieldWithPath("data.id")
                                        .description("id of user"),
                                PayloadDocumentation.fieldWithPath("data.username")
                                        .description("name of user"),
                                PayloadDocumentation.fieldWithPath("data.profileUrl")
                                        .description("profile url of user")
                        )
                ));
    }
}
