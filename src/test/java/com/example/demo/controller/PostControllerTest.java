package com.example.demo.controller;
import com.example.demo.configuration.EncoderConfig;
import com.example.demo.domain.dto.ModifyRequest;
import com.example.demo.domain.dto.PostRequest;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
   MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    EncoderConfig encoderConfig;

    @Autowired
    ObjectMapper objectMapper;
    
    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 작성 성공")
    void post_success() throws Exception {

        PostRequest postRequest = PostRequest.builder()
                .title("title_post")
                .body("body_post")
                .build();

        when(postService.post(any(), any(), any()))
                .thenReturn("포스트 작성 성공");

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser // 인증 된지 않은 상태
    @DisplayName("포스트 작성 실패(1) : 인증 실패")
    void post_fail1() throws Exception {

        PostRequest postRequest = PostRequest.builder()
                .title("title_post")
                .body("body_post")
                .build();

        when(postService.post(any(), any(), any()))
                .thenReturn("포스트 작성 실패");

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 수정 성공")
    void modify_success() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("title_modify")
                .body("body_modify")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenReturn("포스트 수정 성공");

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser // 인증 되지 않은 상태
    @DisplayName("포스트 수정 실패(1) : 인증 실패")
    void modify_fail1() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("title_modify")
                .body("body_modify")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenReturn("포스트 수정 성공");

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 수정 실패(2) : 포스트 내용 불일치")
    void modify_fail2() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("title_modify")
                .body("body_modify")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 수정 실패(3) : 작성자 불일치")
    void modify_fail3() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("title_modify")
                .body("body_modify")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 수정 실패(4) : 데이터베이스 에러")
    void modify_fail4() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("title_modify")
                .body("body_modify")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 삭제 성공")
    void delete_success() throws Exception {

        when(postService.delete(any(), any()))
                .thenReturn("포스트 삭제 성공");

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser // 인증 된지 않은 상태
    @DisplayName("포스트 삭제 실패(1) : 인증 실패")
    void delete_fail1() throws Exception {

        when(postService.delete(any(), any()))
                .thenReturn("포스트 삭제 성공");

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 삭제 실패(2) : 포스트 내용 불일치")
    void delete_fail2() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 삭제 실패(3) : 작성자 불일치")
    void delete_fail3() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 삭제 실패(4) : 데이터베이스 에러")
    void delete_fail4() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }
}
