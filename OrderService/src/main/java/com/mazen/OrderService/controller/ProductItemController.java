package com.mazen.OrderService.controller;

import com.mazen.OrderService.dto.ProductResponse;
import com.mazen.OrderService.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/productItem")
public class ProductItemController {
    private final ProductItemService productItemService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProductItemsByIds(@RequestParam List<Long> ids){
        return ResponseEntity.ok(productItemService.getProductItemsById(ids));
    }

}
