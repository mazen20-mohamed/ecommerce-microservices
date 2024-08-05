package com.example.ReviewService.controller;


import com.example.ReviewService.dto.ProductRateDto;
import com.example.ReviewService.dto.ProductRateRequest;
import com.example.ReviewService.dto.ReviewDto;
import com.example.ReviewService.service.ProductRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/rate")
@RequiredArgsConstructor
public class ProductRateController {

    private final ProductRateService productRateService;

    @GetMapping("/{productId}")
    public ProductRateDto getProductRate(@PathVariable String productId){
        return productRateService.getProductRate(productId);
    }


    @PostMapping
    public void addReviewForProduct(@RequestBody ProductRateRequest productRateRequest){
        productRateService.addReviewForProduct(productRateRequest);
    }


    @GetMapping("/{productId}/{userId}")
    public ReviewDto getReviewOfUser(@PathVariable String productId,@PathVariable String userId){
        return productRateService.getReviewOfUser(productId,userId);
    }

    @GetMapping("/all/{productId}")
    public List<ReviewDto> getAllReviewOfProduct(String productId){
        return productRateService.getAllReviewOfProduct(productId);
    }

}
