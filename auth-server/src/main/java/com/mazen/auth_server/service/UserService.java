package com.mazen.auth_server.service;


import com.mazen.auth_server.dto.LoginRequest;
import com.mazen.auth_server.dto.LoginResponse;
import com.mazen.auth_server.dto.UserRequest;
import com.mazen.auth_server.exception.BadRequestException;
import com.mazen.auth_server.model.Role;
import com.mazen.auth_server.model.User;
import com.mazen.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void createUser(UserRequest userRequest){
        User user = modelMapper.map(userRequest,User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public LoginResponse loginUser(LoginRequest loginRequest){
        User user =  userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->
                new BadRequestException("Email is not correct"));
        if(!user.getPassword().equals(passwordEncoder.encode(loginRequest.getPassword()))){
            throw new BadRequestException("Password is not correct");
        }
        String token = jwtService.generateToken(user,user.getRole());
        return LoginResponse.builder().token(token).build();
    }
}
