package com.example.demo.controller;

import com.example.demo.domain.dto.PostRequest;
import com.example.demo.domain.dto.UserJoinRequest;
import com.example.demo.domain.dto.UserLoginRequest;
import com.example.demo.domain.response.UserJoinResponse;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.descriptor.web.FragmentJarScannerCallback;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    //회원 가입 성공 했을 때 테스트
    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser // @WithAnonymousUser 시, Error 발생
    void join_success() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("user")
                .password("password")
                .build();

        when(userService.join(any(), any()))
                .thenReturn(UserJoinResponse.builder()
                        .userName(userJoinRequest.getUserName())
                        .password(userJoinRequest.getPassword())
                        .build());

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

   //userId가 중복 됐을 때 테스트
    @Test
    @DisplayName("회원가입 실패 : username 중복")
    @WithMockUser
    void join_fail() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("user")
                .password("password")
                .build();

        when(userService.join(any(), any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getHttpStatus().value()));
    }
    
    //로그인 성공했을 때 테스트
    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("user")
                .password("password")
                .build();

        when(userService.login(any(), any()))
                .thenReturn("token");

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //userName이 존재하지 않을 때 테스트
    @Test
    @DisplayName("로그인 실패 - username 없음")
    @WithMockUser
    void login_fail1() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("user")
                .password("password")
                .build();

        when(userService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getHttpStatus().value()));
    }

    //password틀렸을 때 테스트
    @Test
    @DisplayName("로그인 실패 - password틀림")
    @WithMockUser
    void login_fail2() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("user")
                .password("password")
                .build();

        when(userService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getHttpStatus().value()));
    }
}