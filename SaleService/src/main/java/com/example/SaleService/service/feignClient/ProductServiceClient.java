package com.example.SaleService.service.feignClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ProductService")
public interface ProductServiceClient {

    @GetMapping("/v1/products/{product_id}/exists")
    boolean isProductExist(@PathVariable String product_id);

    @GetMapping("/exists")
    List<String> isProductsExists(@RequestParam List<String> ids);
}
