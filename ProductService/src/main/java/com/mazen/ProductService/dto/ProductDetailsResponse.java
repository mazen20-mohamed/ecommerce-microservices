package com.mazen.ProductService.dto;


import com.mazen.ProductService.model.ProductCategory;
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
    private ProductCategory productCategory;
    private String description;
    private List<ProductSpecsResponse> productSpecsResponses;
}
