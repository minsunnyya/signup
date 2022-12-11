package com.example.demo.service;

import com.example.demo.domain.entity.PostEntity;
import com.example.demo.domain.entity.UserEntity;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.fixture.TestInfoFixture;
import com.example.demo.fixture.UserEntityFixture;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;
import static org.mockito.Mockito.*;

@WebMvcTest(PostService.class)
public class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    UserRepository userRepository;


    @MockBean
    PostRepository postRepository;

    //포스트생성시 유저가 존재하지 않을 때 에러
    @Test
    @DisplayName("등록 실패 : 유저 존재하지 않음")
    void post_fail_no_user() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(PostEntity.class));
        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.post(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));

        Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }
    //포스트 수정시 포스트 없을 때 에러
    @Test
    @DisplayName("수정 실패 : 포스트 존재하지 않음")
    @WithMockUser   
    void post_fail_no_post() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class, () ->
                postService.modify(String.valueOf(fixture.getUserId()), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    //포스트 수정시 유저가 존재하지 않을 때 에러
    @Test
    @DisplayName("수정 실패 : 유저 존재하지 않음")
    void modify_fail_no_user() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.modify(String.valueOf(fixture.getUserId()), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    //포스트 삭제시 포스트가 존재하지 않을 때 에러
    @Test
    @DisplayName("삭제 실패 : 포스트 존재하지 않음")
    void delete_fail_no_post() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.delete(fixture.getUserName(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    //포스트 삭제시 유저가 존재하지 않을 때 에러
    @Test
    @DisplayName("삭제 실패 : 유저 존재하지 않음")
    void delet_fail_no_user() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.delete(fixture.getUserName(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    //포스트 삭제시 작성자와 유저가 존재하지 않을 때 에러
    @Test
    @DisplayName("삭제 실패 : 작성자와 유저가 존재하지 않음")
    void delete_fail_no_match() {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);

        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPostEntity));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));
        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.delete(fixture.getUserName(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

}
