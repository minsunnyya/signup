package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.domain.dto.DeleteRequest;
import com.example.demo.domain.dto.ModifyRequest;
import com.example.demo.domain.dto.PostRequest;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.PostService;
import com.example.demo.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<String> posts(@RequestBody PostRequest dto, Authentication authentication){
        System.out.println("Controller Test Enter");
        System.out.println(authentication.getName());
        postService.post(dto.getTitle(),dto.getBody(), authentication.getName());
        System.out.println("Controller Test");
        return ResponseEntity.ok().body("등록 완료");
    }
    @PutMapping("/{postId}")    // postid → string으로만 오는 거 같은데 숫자형태로 올 수 없는지
    public ResponseEntity<String> modify(@PathVariable String postId, @RequestBody ModifyRequest dto, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        postService.modify(user.getId(), Integer.parseInt(postId), dto.getTitle(), dto.getBody());
        return ResponseEntity.ok().body("수정 완료");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> delete(@PathVariable String postId, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        postService.delete(user.getId(), Integer.parseInt(postId));
        return ResponseEntity.ok().body("삭제 완료");
    }
}

