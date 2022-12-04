package com.example.demo.service;


import com.example.demo.domain.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    public String join(String userName, String password){
        userRepository.findByUserName(userName).ifPresent(
                user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, " "+ userName +"는 이미 있습니다.");
                }
        );
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .build();
        userRepository.save(user);
        return "성공";
    }
    public String login(String userName, String password){
    return "token리턴";
    }
}
