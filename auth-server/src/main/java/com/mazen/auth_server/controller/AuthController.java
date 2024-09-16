package com.mazen.auth_server.controller;


import com.mazen.auth_server.dto.LoginRequest;
import com.mazen.auth_server.dto.LoginResponse;
import com.mazen.auth_server.dto.UserRequest;
import com.mazen.auth_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping
    public void addUser(@RequestBody UserRequest userRequest){
        userService.createUser(userRequest);
    }

//    @PostMapping("/login")
//    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest){
//        return userService.loginUser(loginRequest);
//    }


}
