package com.mazen.OrderService.service.feign;


import com.mazen.OrderService.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ProductService")
public interface ProductClient {
    @GetMapping
    List<ProductResponse> getProductsByIds(@RequestParam List<String> ids);

}
