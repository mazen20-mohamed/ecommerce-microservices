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
    private String id;
    private String title;
    private double price;
    private List<String> imagesUrl;
    private ProductCategory productCategory;
    private String description;
    private List<ProductColorResponse> productColorResponses;
}
