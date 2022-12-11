package com.example.demo.controller;

import com.example.demo.domain.dto.UserJoinRequest;
import com.example.demo.domain.dto.UserLoginRequest;
import com.example.demo.domain.response.UserJoinResponse;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest dto){
        UserJoinResponse userJoinResponse = userService.join(dto.getUserName(), dto.getPassword());
    return ResponseEntity.ok().body("회원가입이 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> log(@RequestBody UserLoginRequest dto){
        String token = userService.login(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body(token);
    }

}