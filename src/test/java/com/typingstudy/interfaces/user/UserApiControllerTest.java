package com.typingstudy.interfaces.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Order
 * 1: join
 * 2: login
 * 3: create object
 * other: after those
 */
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
    // init on group creating test
    private Long myFavGroupId;
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

    // returns userId
    private Long joinUser(String email, String username, String password, String profileUrl) throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("username", username);
        body.put("password", password);
        body.put("profileUrl", profileUrl);
        ResultActions result = this.mockMvc.perform(
                        post("/api/v1/user/join")
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        String responseBody = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> json = new JacksonJsonParser().parseMap(responseBody);
        log.info("json={}", json);
        Map<String, Object> data = (Map<String, Object>) json.get("data");
        log.info("data={}", data);
        return Long.parseLong((String) data.get("id"));
    }

    private void createDoc(String title, String content, String access) throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("content", content);
        body.put("access", access);
        this.mockMvc.perform(
                        post("/api/v1/docs")
                                .header("Authorization", updateAccessToken())
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    // add 5 example docs
    private void createDocs() throws Exception {
        for (int i = 0; i < 5; i++) {
            createDoc("example " + i, "this is example " + i, "PUBLIC");
        }
    }

    // returns list of data
    private List<Map<String, Object>> fetchDocs() throws Exception {
        ResultActions result = this.mockMvc.perform(
                get("/api/v1/docs")
                        .header("Authorization", updateAccessToken())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Map<String, Object> json = new JacksonJsonParser().parseMap(result.andReturn().getResponse().getContentAsString());
        log.info("fetch docs={}", json.get("data"));
        return (List<Map<String, Object>>) json.get("data");
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
    @DisplayName("POST /api/v1/user/join")
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
    @DisplayName("POST /login")
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
    @DisplayName("GET /api/v1/user/me")
    void me() throws Exception {
        this.mockMvc.perform(
                        get("/api/v1/user/me")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", updateAccessToken())
                )
                .andExpect(status().isOk())
                .andDo(document("my-user-info",
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

    @Test
    @DisplayName("GET /api/v1/user/{userId}")
    void user_info() throws Exception {
        Long userId = joinUser("other@naver.com", "otheruser", "mypassword1234", "https://www.mypro.com/otheruser.png");
        this.mockMvc.perform(
                        get("/api/v1/user/info/" + userId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("user-info",
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

    @Test
    @Rollback(value = false)
    @Order(3)
    @DisplayName("POST /api/v1/user/favorites/create")
    void create_favorite_group() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("groupName", "my favorite");
        ResultActions result = this.mockMvc.perform(
                        post("/api/v1/user/favorites/create")
                                .content(new ObjectMapper().writeValueAsString(body))
                                .header("Authorization", updateAccessToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("create-favorite-group",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("result")
                                        .description("result"),
                                PayloadDocumentation.fieldWithPath("data.groupId")
                                        .description("id of favorite group"),
                                PayloadDocumentation.fieldWithPath("data.groupName")
                                        .description("name of group"),
                                PayloadDocumentation.fieldWithPath("data.userId")
                                        .description("owner of group")
                        )
                ));
        Map<String, Object> responseBody = new JacksonJsonParser().parseMap(result.andReturn().getResponse().getContentAsString());
        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        log.info("group data={}", data);
        this.myFavGroupId = Long.parseLong(data.get("groupId").toString());
    }

    @Test
    @DisplayName("GET /api/v1/user/favorites")
    void fetch_user_favorites() throws Exception {
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> body = new HashMap<>();
            body.put("groupName", "favorite ex " + i);
            this.mockMvc.perform(
                    post("/api/v1/user/favorites/create")
                            .content(new ObjectMapper().writeValueAsString(body))
                            .header("Authorization", updateAccessToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        }
        this.mockMvc.perform(
                        get("/api/v1/user/favorites")
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("fetch-user-favorite-groups",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("GET /api/v1/user/favorites/{groupId}")
    void fetch_favorite_group() throws Exception {
        if (myFavGroupId == null) {
            create_favorite_group();
        }
        createDocs();
        List<Map<String, Object>> docs = fetchDocs();
        docs.forEach(doc -> {
            HashMap<String, Object> body = new HashMap<>();
            body.put("docToken", doc.get("docToken"));
            try {
                this.mockMvc.perform(
                                post("/api/v1/user/favorites/" + myFavGroupId + "/add")
                                        .header("Authorization", updateAccessToken())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(body))
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        this.mockMvc.perform(
                        get("/api/v1/user/favorites/" + myFavGroupId)
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("fetch-favorite-group",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("POST /api/v1/user/favorites/{groupId}/add")
    void add_favorite_item() throws Exception {
        if (myFavGroupId == null) {
            create_favorite_group();
        }
        createDocs();
        List<Map<String, Object>> docs = fetchDocs();
        HashMap<String, Object> body = new HashMap<>();
        body.put("docToken", docs.get(0).get("docToken"));
        this.mockMvc.perform(
                        post("/api/v1/user/favorites/" + myFavGroupId + "/add")
                                .header("Authorization", updateAccessToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(body))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("add-favorite-item",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("PATCH /api/v1/user/favorites/{groupId}")
    void edit_favorite_group() throws Exception {
        if (myFavGroupId == null) {
            create_favorite_group();
        }
        Map<String, Object> body = new HashMap<>();
        body.put("groupName", "edit favorite name");
        this.mockMvc.perform(
                        patch("/api/v1/user/favorites/" + myFavGroupId)
                                .header("Authorization", updateAccessToken())
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("edit-favorite-group",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("result")
                                        .description("result"),
                                PayloadDocumentation.fieldWithPath("data.groupId")
                                        .description("id of group"),
                                PayloadDocumentation.fieldWithPath("data.groupName")
                                        .description("name of group"),
                                PayloadDocumentation.fieldWithPath("data.userId")
                                        .description("owner of user")
                        )
                ));
    }

    @Test
    @DisplayName("DELETE /api/v1/user/favorites/{groupId}")
    void delete_favorite_group() throws Exception {
        if (myFavGroupId == null) {
            create_favorite_group();
        }
        this.mockMvc.perform(
                        delete("/api/v1/user/favorites/" + myFavGroupId)
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("delete-favorite-group",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @Rollback(value = false)
    @Order(Integer.MAX_VALUE / 2 + 1)
    @DisplayName("POST /api/v1/user/resign")
    void resign_user() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "user1234");
        this.mockMvc.perform(
                        post("/api/v1/user/resign")
                                .header("Authorization", updateAccessToken())
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("resign-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("result")
                                        .description("result"),
                                PayloadDocumentation.fieldWithPath("message")
                                        .description("ok")
                        )
                ));
    }
}
