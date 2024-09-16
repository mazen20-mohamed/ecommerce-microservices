package com.mazen.auth_server.security;

import com.mazen.auth_server.service.JwtService;
import com.mazen.auth_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception{

        // configure authenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);


        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);

        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(req->
                req.requestMatchers(HttpMethod.POST, "/v1/auth/**").permitAll());

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager,userService,jwtService);
        authenticationFilter.setFilterProcessesUrl("/v1/auth/login");
        http.addFilter(authenticationFilter)
                .authenticationManager(authenticationManager);

        http.sessionManagement(session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
