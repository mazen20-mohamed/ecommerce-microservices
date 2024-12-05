package com.mazen.OrderService.service.feign;


import com.mazen.OrderService.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AuthServer")
public interface UserClient {

    @GetMapping("/v1/user/{userId}")
    UserResponseDTO getUserData(@PathVariable String userId);
}
