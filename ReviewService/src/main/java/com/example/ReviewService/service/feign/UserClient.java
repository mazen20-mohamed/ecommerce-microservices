package com.example.ReviewService.service.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AuthServer")
public interface UserClient {

    @GetMapping("/v1/user/{userId}")
    void getUserData(@PathVariable String userId);
}
