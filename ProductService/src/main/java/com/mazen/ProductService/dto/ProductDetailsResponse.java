package com.mazen.ProductService.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponse {
    private String title;
    private double price;
    private double priceAfterDiscount;
    private String productCategory;
    private String description;
    private List<ProductSpecsResponse> productSpecsResponses;
}
