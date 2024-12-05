package com.example.ReviewService.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ProductService")
public interface ProductClient {
    @GetMapping("/{productId}")
    void getProductById(@PathVariable String productId);

}
