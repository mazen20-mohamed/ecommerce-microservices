package com.mazen.Cart.And.WishList.Service.service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AuthServer")
public interface UserClient {

    @GetMapping("/v1/users/{userId}")
    void getUser(@PathVariable String userId, @RequestHeader("Authorization") String authorization);


}
