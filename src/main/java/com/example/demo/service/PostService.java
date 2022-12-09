package com.example.demo.service;

import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.domain.entity.PostEntity;
import com.example.demo.domain.entity.UserEntity;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void post(String title, String body, String userName) {
        System.out.println("Service Test Post Enter");

        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        PostEntity post = PostEntity.of(title, body, userEntity.getUserName());

        postRepository.save(post);
        System.out.println("Service Test Post");
    }

    public String modify(Integer userId, Integer postId, String title, String body) {

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userId)));;
        System.out.println("test3");
        if (!Objects.equals(postEntity.getUsername(), userEntity.getUserName())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
        }
        System.out.println("test4");

        postEntity.setTitle(title);
        postEntity.setBody(body);

        postRepository.saveAndFlush(postEntity);

        return "Service Test Post2";
    }

    public String delete(Integer userId, Integer postId) {

        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userId)));;
        if (!Objects.equals(postEntity.getUsername(), userEntity.getUserName())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
        }

        postRepository.delete(postEntity);

        return "삭제 처리";
    }
}
