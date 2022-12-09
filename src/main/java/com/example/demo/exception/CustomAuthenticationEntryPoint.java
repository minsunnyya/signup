package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws AppException, IOException, ServletException {
//        response.setContentType("application/json");
//        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
//        response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.name()).toStream());
        // 예외 처리 방법 모르겠음.
        System.out.println("인증 실패");

        throw new AppException(ErrorCode.INVALID_TOKEN, " ");
    }
}
