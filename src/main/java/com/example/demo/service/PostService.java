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
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

        @Transactional
    public void post(String title, String body, String username) {
        UserEntity userEntity = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", username)));
        System.out.println("Service Test Post1");
//        PostEntity post = PostEntity.of(title, body, userEntity.getUserName());
        PostEntity post = PostEntity.of(title, body, userEntity);

        postRepository.save(post);
        System.out.println("Service Test Post2");
    }

    @Transactional
    public String modify(String userName, Integer postId, String title, String body) {
        System.out.println("Modify Service Tes1");
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        System.out.println("Modify PostEntity");

        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        Integer userId = userEntity.getId();

        if (!Objects.equals(postEntity.getUser().getId(), userId)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
        }
        System.out.println("test4");

        postEntity.setTitle(title);
        postEntity.setBody(body);

        postRepository.saveAndFlush(postEntity);

        return "Service Test Post2";
    }

    @Transactional
    public String delete(String userName, Integer postId) {
        System.out.println("Delete Service Tes1");
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        System.out.println("Delete PostEntity");

        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        Integer userId = userEntity.getId();

        if (!Objects.equals(postEntity.getUser(), userId)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
        }

        postRepository.delete(postEntity);

        return "삭제 처리";
    }
}
