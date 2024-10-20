package com.mazen.Cart.And.WishList.Service.service.feignClient;

import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ProductService")
public interface ProductClient {

    @GetMapping("/v1/products/{id}")
    void getProductDetailsById(@PathVariable String id,
                               @RequestHeader("Authorization") String authorization);

    @GetMapping("/v1/products")
    List<ProductResponse> getProductsByIds(@RequestParam List<String> ids
            ,@RequestHeader("Authorization") String authorization);

}
