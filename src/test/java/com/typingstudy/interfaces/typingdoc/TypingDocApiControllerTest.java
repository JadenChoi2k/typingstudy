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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
@ActiveProfiles({"test", "oauth", "mail"})
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TypingDocApiControllerTest {
    private MockMvc mockMvc;
    private String authToken;
    private static boolean joinFlag = false;
    private static boolean docCreateFlag = false;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(documentationConfiguration(restDocumentation))
//                .alwaysDo(document("{method-name}",
//                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @BeforeEach
    private void before_create_doc() throws Exception {
        if (!joinFlag) {
            joinUser();
        }
        if (!docCreateFlag) {
            create_doc();
        }
    }

    private Long joinUser() throws Exception {
        log.info("try to join user, joinFlag={}", joinFlag);
        Long userId = joinUser("user1234@gmail.com", "user1234", "1q2w3e4r", "https://www.mypro.com/user1234.png");
        log.info("complete join, joinFlag={}", joinFlag);
        return userId;
    }

    private Long joinUser(String email, String username, String password, String profileUrl) throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("username", username);
        body.put("password", password);
//        body.put("profileUrl", profileUrl);
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
                .andExpect(status().isOk());
//                .andExpect(header().exists("Authorization"));
        Map<String, Object> json = new JacksonJsonParser().parseMap(result.andReturn().getResponse().getContentAsString());
        return json.get("accessToken").toString();
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
    @Order(value = Integer.MAX_VALUE)
    @Rollback(value = false)
    @DisplayName("only for rollback")
    void rollback() throws Exception {
        // remove docs
        List<String> docTokenList = getDocTokenList();
        docTokenList.forEach(
                token -> {
                    try {
                        this.mockMvc.perform(
                                        delete("/api/v1/docs/" + token)
                                                .header("Authorization", updateAccessToken())
                                                .accept(MediaType.APPLICATION_JSON)
                                ).andExpect(status().isOk())
                                .andDo(document("delete-doc",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint())
                                ));
                    } catch (Exception e) {
                        // just pass
                    }
                }
        );
        // resign user
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("username", "user1234");
            this.mockMvc.perform(
                    post("/api/v1/user/resign")
                            .header("Authorization", updateAccessToken())
                            .content(new ObjectMapper().writeValueAsString(body))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );
        } catch (Exception e) {
            // if failed, just pass
        }
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
        log.info("before request");
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
    @DisplayName("REQUEST /api/v1/docs/{docToken}/review")
    void review_doc() throws Exception {
        List<String> docTokenList = getDocTokenList();
        String token = docTokenList.get(docTokenList.size() - 1);
        this.mockMvc.perform(
                        get("/api/v1/docs/" + token + "/review")
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("review-doc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("GET /api/v1/docs/{docToken}/history")
    void fetch_doc_history() throws Exception {
        List<String> docTokenList = getDocTokenList();
        String token = docTokenList.get(docTokenList.size() - 1);
        for (int i = 0; i < 3; i++) {
            this.mockMvc.perform(
                            get("/api/v1/docs/" + token + "/review")
                                    .header("Authorization", updateAccessToken())
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk());
        }
        this.mockMvc.perform(
                        get("/api/v1/docs/" + token + "/history")
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("doc-history",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("result")
                                        .description("result"),
                                PayloadDocumentation.fieldWithPath("data.size")
                                        .description("whole size of history"),
                                PayloadDocumentation.fieldWithPath("data.data[].reviewAt")
                                        .description("datetime of review"),
                                PayloadDocumentation.fieldWithPath("data.data[].docToken")
                                        .description("token of doc")
                        )
                ));
    }

    @Test
    @DisplayName("GET /api/v1/docs/history")
    void fetch_user_doc_history() throws Exception {
        if (!docCreateFlag) {
            create_doc();
        }
        List<String> docTokenList = getDocTokenList();
        docTokenList.forEach(token -> {
            try {
                this.mockMvc.perform(
                                get("/api/v1/docs/" + token + "/review")
                                        .header("Authorization", updateAccessToken())
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        this.mockMvc.perform(
                        get("/api/v1/docs/history")
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("doc-history",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("result")
                                        .description("result"),
                                PayloadDocumentation.fieldWithPath("data.size")
                                        .description("whole size of history"),
                                PayloadDocumentation.fieldWithPath("data.data[].reviewAt")
                                        .description("datetime of review"),
                                PayloadDocumentation.fieldWithPath("data.data[].docToken")
                                        .description("token of doc")
                        )
                ));
    }

    @Test
    @DisplayName("GET /api/v1/docs/{docToken}/comment")
    void fetch_doc_comment() throws Exception {
        // add comment
        String token = getExampleToken();
        addExampleComment(token);
        this.mockMvc.perform(
                        get("/api/v1/docs/" + token + "/comment")
                                .header("Authorization", updateAccessToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("fetch-doc-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("result")
                                        .description("result"),
                                PayloadDocumentation.fieldWithPath("data[].id")
                                        .description("id of comment"),
                                PayloadDocumentation.fieldWithPath("data[].docToken")
                                        .description("token of doc"),
                                PayloadDocumentation.fieldWithPath("data[].content")
                                        .description("content of comment"),
                                PayloadDocumentation.fieldWithPath("data[].userId")
                                        .description("userId of comment"),
                                PayloadDocumentation.fieldWithPath("data[].editedAt")
                                        .description("datetime of edit")
                        )
                ));
    }

    @Test
    @DisplayName("POST /api/v1/docs/{docToken}/comment")
    void add_doc_comment() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("content", "I like this");
        this.mockMvc.perform(
                        post("/api/v1/docs/" + getExampleToken() + "/comment")
                                .header("Authorization", updateAccessToken())
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("add-doc-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        getCommentDtoResponseFields()
                ));
    }

    @Test
    @DisplayName("PATCH /api/v1/docs/{docToken}/comment/{commentId}")
    void edit_doc_comment() throws Exception {
        // add comment
        String token = getExampleToken();
        addExampleComment(token);
        // fetch comment
        Long commentId = getExampleCommentId(token);
        // do test
        Map<String, Object> body = new HashMap<>();
        body.put("content", "I edited comment");
        this.mockMvc.perform(
                        patch("/api/v1/docs/" + token + "/comment/" + commentId)
                                .header("Authorization", updateAccessToken())
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("edit-doc-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        getCommentDtoResponseFields()
                ));
    }

    @Test
    @DisplayName("DELETE /api/v1/docs/{docToken}/comment/{commentId}")
    void delete_doc_comment() throws Exception {
        // add comment
        String token = getExampleToken();
        addExampleComment(token);
        // fetch comment
        Long commentId = getExampleCommentId(token);
        // do test
        this.mockMvc.perform(
                        delete("/api/v1/docs/" + token + "/comment/" + commentId)
                                .header("Authorization", updateAccessToken())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("delete-doc-comment",
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
                fieldWithPath("data.reviewCount")
                        .description("review count of typing doc"),
                fieldWithPath("data.lastStudyDate")
                        .description("last study date"),
                fieldWithPath("data.createDate")
                        .description("created date"),
                fieldWithPath("data.editDate")
                        .description("edited date")
        );
    }

    private ResponseFieldsSnippet getCommentDtoResponseFields() {
        return PayloadDocumentation.responseFields(
                PayloadDocumentation.fieldWithPath("result")
                        .description("result"),
                PayloadDocumentation.fieldWithPath("data.docToken")
                        .description("token of doc"),
                PayloadDocumentation.fieldWithPath("data.id")
                        .description("id of comment"),
                PayloadDocumentation.fieldWithPath("data.content")
                        .description("content of comment"),
                PayloadDocumentation.fieldWithPath("data.userId")
                        .description("id of user"),
                PayloadDocumentation.fieldWithPath("data.editedAt")
                        .description("datetime of edit")
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

    private void addExampleComment(String token) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("content", "I like this");
        this.mockMvc.perform(
                        post("/api/v1/docs/" + token + "/comment")
                                .header("Authorization", updateAccessToken())
                                .content(new ObjectMapper().writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    private String getExampleToken() throws Exception {
        List<String> docTokenList = getDocTokenList();
        return docTokenList.get(docTokenList.size() - 1);
    }

    private Long getExampleCommentId(String token) throws Exception {
        ResultActions result = this.mockMvc.perform(
                        get("/api/v1/docs/" + token + "/comment")
                                .header("Authorization", updateAccessToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        log.info("result json={}", result.andReturn().getResponse().getContentAsString());
        List<Map<String, Object>> comments =
                (List<Map<String, Object>>) new JacksonJsonParser()
                        .parseMap(result.andReturn().getResponse().getContentAsString()).get("data");
        Long commentId = Long.parseLong(comments.get(0).get("id").toString());
        return commentId;
    }
}
