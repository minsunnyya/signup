package com.example.demo.controller;

import com.example.demo.configuration.SecurityConfig;
import com.example.demo.domain.dto.ModifyRequest;
import com.example.demo.domain.dto.PostRequest;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
//@WebMvcTest
//@ContextConfiguration(classes = {SecurityConfig.class})
public class PostControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    //포스트 작성 성공
    @Test
    //    @WithAnonymousUser // 인증 된지 않은 상태
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 작성 성공")
    void post_success() throws Exception {
        String title ="title";
        String body = "body";
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isOk());
    }
    //포스트 수정 성공
    @Test
    @WithMockUser
    @DisplayName("포스트 수정 성공")
    void modity_success() throws Exception {
        String title ="title2";
        String body = "body2";
        mockMvc.perform(put("/api/v1/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new PostRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    @DisplayName("포스트 삭제 성공")
    void delete_success() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isOk());
    }
    //로그인한 상태가 아닐 때 작성 실패 테스트
    @Test
    @WithAnonymousUser
    @DisplayName("포스트작성실패(1)_로그인한상태가_아니라면_에러발생")
    void post_fail1() throws Exception {
        String title ="title";
        String body = "body";
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new ModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    //로그인한 상태가 아닐 때 수정 실패 테스트
    @Test
    @WithAnonymousUser
    @DisplayName("포스트수정실패(1)_로그인한상태가_아니라면_에러발생")
    void modify_fail1() throws Exception {
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getHttpStatus().value()));
    }

    //본인이 작성한 글이 아닐 때 작성 실패 테스트
    @Test
    @WithMockUser
    @DisplayName("포스트수정실패(2)_본인이_작성한_글이_아니라면_에러발생")
    void modify_fail2() throws Exception {
//        doThrow(new AppException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(any(), eq(4), eq("title"), eq("body"));

        String title = "title";
        String body = "body";

        when(postService.modify(any(), eq(4), eq("title"), eq("body")))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(put("/api/v1/posts/4")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new ModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    //수정하는 글이 없을 때 테스트
    @Test
    @WithMockUser
    @DisplayName("포스트수정실패(3)_수정하려는글이_없다면_에러발생")
    void modify_fail3() throws Exception {
        doThrow(new AppException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(any(), any(), any(), any());

        mockMvc.perform(put("/api/v1/posts/100")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new ModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }
    
    //데이터 베이스 에러 났을 때 테스트
    @Test
    @WithMockUser
    @DisplayName("포스트수정실패(4)_데이터베이스_에러_발생시_에러발생")
    void modify_fail4() throws Exception {
        doThrow(new AppException(ErrorCode.DATABASE_ERROR)).when(postService).modify(any(), eq(1), eq("title"), eq("body"));
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new ModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }
    
    //로그인 한 상태가 아닐 때 테스트
    @Test
    @WithAnonymousUser
    @DisplayName("포스트삭제실패(1)_로그인한상태가_아니라면_에러발생")
    void delete_fail1() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getHttpStatus().value()));
    }
    
    //본인이 작성한 글이 아닐 때 테스트
    @Test
    @WithMockUser
    @DisplayName("포스트삭제실패(2)_본인이_작성한_글이_아니라면_에러발생")
    void delete_fail2() throws Exception {
        doThrow(new AppException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());
        mockMvc.perform(delete("/api/v1/posts/4")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }
    
    //수정하는 글이 없을 때 테스트
    @Test
    @WithMockUser
    @DisplayName("포스트삭제실패(3)_수정하려는글이_없다면_에러발생")
    void delete_fail3() throws Exception {
        doThrow(new AppException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    //데이터베이스 에러 테스트
    @Test
    @WithMockUser
    @DisplayName("포스트삭제실패(4)_데이터베이스_에러_발생시_에러발생")
    void delete_fail4() throws Exception {
        doThrow(new AppException(ErrorCode.DATABASE_ERROR)).when(postService).delete(any(), eq(1));
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }
}
