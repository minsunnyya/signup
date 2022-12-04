package com.example.demo.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;
}
