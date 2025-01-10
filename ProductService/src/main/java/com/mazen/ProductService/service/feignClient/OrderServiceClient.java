package com.mazen.ProductService.service.feignClient;


import com.mazen.ProductService.dto.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "OrderStorage")
public interface OrderServiceClient {
    @GetMapping("/v1/orders/product/{productId}")
    List<Object> getOrdersByProductId(@PathVariable String productId ,
                                             @RequestParam List<OrderStatus> orderStatus);
}
