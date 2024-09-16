package com.mazen.UserService.controller;

import com.mazen.UserService.dto.LoginRequest;
import com.mazen.UserService.dto.UserRequestDTO;
import com.mazen.UserService.dto.UserResponseDTO;
import com.mazen.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userRequestDTO){
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/check/{id}")
    public boolean checkUser(@PathVariable String id){
        return userService.checkUser(id);
    }

}
