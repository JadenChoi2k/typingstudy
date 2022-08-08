package com.typingstudy.interfaces.typingdoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test Order
 * 1: create object
 * 2: edit object
 * other: after those
 */
@Slf4j
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TypingDocApiControllerTest {
    private MockMvc mockMvc;
    private String authToken;
    private boolean joinFlag = false;
    private boolean docCreateFlag = false;

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

    private Long joinUser() throws Exception {
        return joinUser("user1234@gmail.com", "user1234", "1q2w3e4r", "https://www.mypro.com/user1234.png");
    }

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
        joinFlag = true;
        return Long.parseLong((String) data.get("id"));
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

    // email: user1234@gmail.com
    // password: 1q2w3e4r
    private String updateAccessToken() throws Exception {
        if (!joinFlag) {
            joinUser();
        }
        if (authToken == null) {
            this.authToken = obtainAccessToken("user1234@gmail.com", "1q2w3e4r");
        }
        return this.authToken;
    }

    @Test
    void test() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/docs/test")
                        .header("Authorization", updateAccessToken())
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("POST /api/v1/docs")
    void create_doc() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("title", "doc example");
        body.put("content", "this is example doc");
        body.put("access", "공개");
        this.mockMvc.perform(
                        post("/api/v1/docs")
                                .content(new ObjectMapper().writeValueAsString(body))
                                .header("Authorization", updateAccessToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("create-doc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        docDtoResponseFields()
                ));
        for (int i = 1; i <= 4; i++) {
            body = new HashMap<>();
            body.put("title", "doc example " + i);
            body.put("content", "this is example doc " + i);
            body.put("access", "공개");
            this.mockMvc.perform(
                    post("/api/v1/docs")
                            .content(new ObjectMapper().writeValueAsString(body))
                            .header("Authorization", updateAccessToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );
        }
        docCreateFlag = true;
    }

    @Test
    @DisplayName("GET /api/v1/docs")
    void fetch_docs() throws Exception {
        if (!docCreateFlag) {
            create_doc();
        }
        this.mockMvc.perform(
                        get("/api/v1/docs?page=0&sort=createdAt&direction=desc")
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("fetch-docs",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("PATCH /api/v1/docs/{docToken}")
    void edit_doc() throws Exception {
        if (!docCreateFlag) {
            create_doc();
        }
        List<String> docTokenList = getDocTokenList();
        String token = docTokenList.get(docTokenList.size() - 1);
        Map<String, Object> body = new HashMap<>();
        body.put("title", "edit title example");
        body.put("content", "edit content example");
        this.mockMvc.perform(
                        patch("/api/v1/docs/" + token)
                                .header("Authorization", updateAccessToken())
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("edit-doc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        docDtoResponseFields()
                ));
    }

    @Test
    @DisplayName("DELETE /api/v1/docs/{docToken}")
    void delete_doc() throws Exception {
        if (!docCreateFlag) {
            create_doc();
        }
        List<String> docTokenList = getDocTokenList();
        String token = docTokenList.get(docTokenList.size() - 1);
        this.mockMvc.perform(
                        delete("/api/v1/docs/" + token)
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(document("delete-doc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("GET /api/v1/docs/bytokens")
    void fetch_docs_by_tokens() throws Exception {
        if (!docCreateFlag) {
            create_doc();
        }
        List<String> docTokenList = getDocTokenList();
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("token", docTokenList);
        this.mockMvc.perform(
                        get("/api/v1/docs/bytokens")
                                .params(params)
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("fetch-docs-by-tokens",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("GET /api/v1/docs/{docToken}")
    void fetch_one_doc() throws Exception {
        if (!docCreateFlag) {
            create_doc();
        }
        List<String> docTokenList = getDocTokenList();
        this.mockMvc.perform(
                        get("/api/v1/docs/" + docTokenList.get(docTokenList.size() - 1))
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("fetch-one-doc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        docDtoResponseFields()
                ));
    }

    @Test
    @DisplayName("GET /api/v1/docs/{docToken}/history")
    void fetch_doc_history() throws Exception {

    }

    @Test
    @DisplayName("GET /api/v1/docs/history")
    void fetch_user_doc_history() throws Exception {

    }

    @Test
    @DisplayName("GET /api/v1/docs/{docToken}/comment")
    void fetch_doc_comment() throws Exception {

    }

    @Test
    @DisplayName("POST /api/v1/docs/{docToken}/comment")
    void add_doc_comment() throws Exception {

    }

    @Test
    @DisplayName("PATCH /api/v1/docs/{docToken}/comment/{commentId}")
    void edit_doc_comment() throws Exception {

    }

    @Test
    @DisplayName("DELETE /api/v1/docs/{docToken}/comment/{commentId}")
    void delete_doc_comment() throws Exception {

    }

    @Test
    @DisplayName("POST /api/v1/docs/{docToken}/obj")
    void add_doc_object() throws Exception {

    }

    @Test
    @DisplayName("GET /api/v1/docs/{docToken}/obj/{fileName}")
    void fetch_doc_object() throws Exception {

    }

    private ResponseFieldsSnippet docDtoResponseFields() {
        return responseFields(
                fieldWithPath("result")
                        .description("creation result"),
                fieldWithPath("data.docToken")
                        .description("token of typing doc"),
                fieldWithPath("data.authorId")
                        .description("id of author"),
                fieldWithPath("data.title")
                        .description("title of typing doc"),
                fieldWithPath("data.content")
                        .description("content of typing doc"),
                fieldWithPath("data.access")
                        .description("access type"),
                fieldWithPath("data.comments")
                        .description("comments of typing doc"),
                fieldWithPath("data.views")
                        .description("view count of typing doc"),
                fieldWithPath("data.lastStudyDate")
                        .description("last study date"),
                fieldWithPath("data.createDate")
                        .description("created date"),
                fieldWithPath("data.editDate")
                        .description("edited date")
        );
    }

    private List<String> getDocTokenList() throws Exception {
        ResultActions result = this.mockMvc.perform(
                        get("/api/v1/docs?page=0&sort=createdAt&direction=desc")
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        List<String> docTokenList = ((List<Map<String, Object>>) new JacksonJsonParser().parseMap(
                result.andReturn().getResponse().getContentAsString()
        ).get("data")).stream()
                .map(doc -> (String) doc.get("docToken")).toList();
        return docTokenList;
    }
}
