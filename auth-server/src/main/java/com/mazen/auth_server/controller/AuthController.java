package com.mazen.auth_server.controller;


import com.mazen.auth_server.dto.LoginRequest;
import com.mazen.auth_server.dto.LoginResponse;
import com.mazen.auth_server.dto.UserRequest;
import com.mazen.auth_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public void addUser(@RequestBody UserRequest userRequest){
//        userService.createUser(userRequest);
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }


}
