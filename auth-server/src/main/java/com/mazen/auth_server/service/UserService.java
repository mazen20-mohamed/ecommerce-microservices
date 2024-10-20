package com.mazen.auth_server.service;
import com.mazen.auth_server.dto.UserDetailsDTO;
import com.mazen.auth_server.dto.UserRequest;
import com.mazen.auth_server.exception.NotFoundException;
import com.mazen.auth_server.model.User;
import com.mazen.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserRequest userRequest){
        User user = modelMapper.map(userRequest,User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("Not found user with this email"));
    }

    public UserDetailsDTO getUser(String userId){
        User user =  userRepository.findById(userId).orElseThrow(()->
                new NotFoundException("Not found user with this id"+userId));
        return new UserDetailsDTO(user.getId(),user.getFirstName(),user.getLastName()
                    ,user.getEmail());
    }

}