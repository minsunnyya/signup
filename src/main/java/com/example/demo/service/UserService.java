package com.example.demo.service;


//import com.example.demo.configuration.filter.JwtTokenfilter;
import com.example.demo.domain.User;
import com.example.demo.domain.entity.UserEntity;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.JwtTokenUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class UserService {

    //    private final JwtTokenUtils jwtTokenUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String secretKey;
    private Long expireTimeMs = 1000 * 60 *60l;


    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+ "이 없습니다." ));

        User user = User.fromEntity(userEntity);

        return user;
    }

    public String join(String userName, String password){
        userRepository.findByUserName(userName).ifPresent(
                user -> {
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, " "+ userName +"는 이미 있습니다.");
                }
        );
        UserEntity user = UserEntity.of(userName, encoder.encode(password));
        userRepository.save(user);
        return "성공";
    }
    public String login(String userName, String password){
        //userName 없음
        User savedUser = loadUserByUsername(userName);

        //password 틀림
        if(!encoder.matches(password,savedUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 잘못 되었습니다.");
        }

        String token = JwtTokenUtils.generateAccessToken(savedUser.getUsername(), secretKey, expireTimeMs);

        return token;
    }
}

