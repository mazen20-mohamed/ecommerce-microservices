package com.mazen.auth_server.controller;


import com.mazen.auth_server.dto.UserDetailsDTO;
import com.mazen.auth_server.repository.UserRepository;
import com.mazen.auth_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDetailsDTO getUser(@PathVariable String userId){
        return userService.getUser(userId);
    }

}
