package com.mazen.UserService.service;

import com.mazen.UserService.dto.LoginRequest;
import com.mazen.UserService.dto.UserRequestDTO;
import com.mazen.UserService.dto.UserResponseDTO;
import com.mazen.UserService.exceptions.BadRequestException;
import com.mazen.UserService.exceptions.NotFoundException;
import com.mazen.UserService.model.User;
import com.mazen.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public String createUser(UserRequestDTO requestDTO){
        User user = modelMapper.map(requestDTO, User.class);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user = userRepository.save(user);
        String id = user.getId();
        log.info("New User created with id "+id);
        return id;
    }

    public String loginUser(LoginRequest loginRequest){
        log.info("Hello");
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if(user.isEmpty()){
            throw new NotFoundException("Email is invalid");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.get().getPassword())){
            throw new BadRequestException("Password is invalid");
        }
        log.info("Hello");
        String token = jwtService.generateToken(user.get());
        log.info(token);
        return token;
    }

    public UserResponseDTO getUserById(String id){
        User user = userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));
        return  modelMapper.map(user,UserResponseDTO.class);
    }

    public boolean checkUser(String id){
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

}
