package com.mazen.auth_server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mazen.auth_server.dto.LoginRequest;
import com.mazen.auth_server.model.User;
import com.mazen.auth_server.service.JwtService;
import com.mazen.auth_server.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private JwtService jwtService;


    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                JwtService jwtService){
        super(authenticationManager);
        this.jwtService = jwtService;
        this.userService = userService;
    }


    // this method call by spring when user make a login, not manually
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword(),new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        String userName = ((User)authResult.getPrincipal()).getUsername();
        User user =  userService.getUserByEmail(userName);

        String token = jwtService.generateToken(user);

        response.addHeader("token",token);
    }
}
