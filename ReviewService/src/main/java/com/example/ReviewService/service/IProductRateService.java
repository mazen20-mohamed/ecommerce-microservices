package com.example.ReviewService.service;

import com.example.ReviewService.dto.ProductRateRequest;
import com.example.ReviewService.dto.ReviewDto;

import java.util.List;

public interface IProductRateService {

    void addReviewForProduct(ProductRateRequest productRateRequest);
    ReviewDto getReviewOfUser(String productId, String userId);
    List<ReviewDto> getAllReviewOfProduct(String productId);
}
