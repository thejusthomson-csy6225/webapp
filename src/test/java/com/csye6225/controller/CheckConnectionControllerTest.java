package com.csye6225.controller;

import com.csye6225.service.CheckConnectionService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CheckConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckConnectionService checkConnectionService;

    @Test
    public void testCheckConnection_Successful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/healthz"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    public void testCheckConnection_WithRequestBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/healthz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckConnection_InvalidUrl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/healthzz")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCheckConnection_InvalidMethod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/healthz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testCheckConnection_Exception() throws Exception {
        doThrow(new RuntimeException("Simulated connection failure"))
                .when(checkConnectionService).checkConnection();
        mockMvc.perform(MockMvcRequestBuilders.get("/healthz"))
                .andExpect(status().isServiceUnavailable());
    }
}
