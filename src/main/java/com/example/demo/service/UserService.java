package com.example.demo.service;


import com.example.demo.domain.User;
import com.example.demo.repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final com.example.demo.repository.userRepository userRepository;
    public String join(String userName, String password){
        userRepository.findByUserName(userName).ifPresent(
                user -> {
                    throw new RuntimeException(userName + "는 이미 있습니다.");
                }
        );
        User user = User.builder()
                .userName(userName)
                .password(password)
                .build();
        userRepository.save(user);
        return "성공";
    }
}
