package com.mazen.ProductService.service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "SaleService")
public interface SaleServiceClient {

    @GetMapping("/v1/discount/{productId}")
    int getProductDiscountById(@PathVariable String productId,@RequestHeader("Authorization") String authorization);


}
