package com.typingstudy.interfaces.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {
    @Autowired
    private UserApiController userApiController;
    @Autowired
    private WebTestClient webTestClient;
    private MockMvc mockMvc;
    private final Map<String, String> testJoin = Map.of(
            "email", "useremail@test.com",
            "password", "password",
            "username", "testUser",
            "profileUrl", "image"
    );
    private final Map<String, String> testLogin = Map.of(
            "email", "useremail@test.com",
            "password", "password"
    );

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userApiController)
                .alwaysExpect(MockMvcResultMatchers.status().isOk())
                .build();
        // join and login
//        mockMvc.
    }

    @Test
    void mockTest() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/v1/user/test")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("test"));
    }

    @Test
    void me() throws Exception {
//        mockMvc.perform(
//                MockMvcRequestBuilders
//                        .get("/api/v1/user/me")
//        )
    }
}
