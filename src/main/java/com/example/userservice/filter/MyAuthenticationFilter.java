package com.example.userservice.filter;

import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserService userService;
    private Environment env;

    public MyAuthenticationFilter(UserService userService,
                                  Environment env,
                                  AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.env = env;
        super.setAuthenticationManager(authenticationManager);
    }

    // http://localhost:8000/login
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException {
        try {
            //json data => RequestLogin 객체 변환
            RequestLogin requestLogin =
                    new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestLogin.getEmail(),
                            requestLogin.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        log.info(">>> 인증 성공");
    }
}
