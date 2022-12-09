//package com.example.demo.controller;
//
//import com.example.demo.domain.dto.ModifyRequest;
//import com.example.demo.domain.dto.PostRequest;
//import com.example.demo.domain.dto.UserJoinRequest;
//import com.example.demo.exception.ErrorCode;
//import com.example.demo.service.PostService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithAnonymousUser;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest
//@WebAppConfiguration
//public class PostControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private PostService postService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("작성성공")
//    @WithMockUser
//
//    void post_success() throws Exception {
//        String title = "title";
//        String body = "body";
//
//        mockMvc.perform(post("/api/v1/post")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new PostRequest(title, body))))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    @DisplayName("작성실패")
//    @WithMockUser
//    void post_no_user_fail() throws Exception {
//        String title = "title";
//        String body = "body";
//
//        mockMvc.perform(post("/api/v1/post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new PostRequest(title, body))))
//                .andDo(print())
//                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getHttpStatus().value()));
//
//    }
//}
