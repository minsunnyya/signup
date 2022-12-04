package com.example.demo.controller;

import com.example.demo.domain.dto.UserJoinRequest;
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
//    @GetMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest dto){
         userService.join(dto.getUserName(), dto.getPassword());
//         System.out.println("Controller End");
    return ResponseEntity.ok().body("회원가입이 성공");
    }

}