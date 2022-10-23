package com.example.jwtdemo.controller;

import com.example.jwtdemo.config.SecurityConfig;
import com.example.jwtdemo.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({HomeController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
class HomeControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        MvcResult result = this.mvc.perform(post("/token")
                        .with(httpBasic("user", "password")))
                        .andExpect(status().isOk())
                        .andReturn();

        String token = result.getResponse().getContentAsString();

        this.mvc.perform(get("/")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(content().string("Hello user"));
    }

    @Test
    @WithMockUser
    void rootWithMockUserStatusIsOk() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

}