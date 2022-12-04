package com.example.demo.controller;

import com.example.demo.domain.dto.UserJoinRequest;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

//    @Test
//    @DisplayName("회원가입 성공")
//    void join() throws Exception {
//        String userName = "minsun";
//        String password = "1234";
//        mockMvc.perform(post("/api/v1/users/join")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//    @Test
//    @DisplayName("회원가입 실패 - userName중복")
//    void join_fail() throws Exception {
//        String userName = "minsun";
//        String password = "1234";
//        mockMvc.perform(post("/api/v1/users/join")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
//                .andDo(print())
//                .andExpect(status().isConflict());
//
//    }
}