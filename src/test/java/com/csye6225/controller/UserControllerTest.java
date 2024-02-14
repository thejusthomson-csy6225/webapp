package com.csye6225.controller;

import com.csye6225.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void insertUserDetails_Success() throws Exception {
        mockMvc.perform(post("/v1/user")
                .content(createJsonInsert(new User(null, "Thejus", "Thomson", "password","thejus@gmail.com", null, null)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("thejus@gmail.com", "password");
        mockMvc.perform(get("/v1/user/self")
                .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("thejus@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Thejus"))
                .andExpect(jsonPath("$.lastName").value("Thomson"))
        ;

        mockMvc.perform(delete("/v1/user/{username}", "thejus@gmail.com")
        ).andExpect(status().isNoContent());

    }

    @Test
    void updateUserDetails_Success() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("thejus@gmail.com", "password");
        mockMvc.perform(post("/v1/user")
                .content(createJsonInsert(new User(null, "Thejus", "Thomson", "password","thejus@gmail.com", null, null)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/v1/user/self")
                .content(createJsonInsert(new User(null, "Wilson", "Jacob", "drowssap",null, null, null)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        headers.setBasicAuth("thejus@gmail.com", "drowssap");

        mockMvc.perform(get("/v1/user/self")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Wilson"))
                .andExpect(jsonPath("$.lastName").value("Jacob"))
        ;

        mockMvc.perform(delete("/v1/user/{username}", "thejus@gmail.com")
        ).andExpect(status().isNoContent());
    }

    private String createJsonInsert(User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }
}