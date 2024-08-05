package com.mazen.ProductService.dto;


import com.mazen.ProductService.model.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String title;
    private double price;
    private ProductCategory productCategory;
    private String imageUrl;
}
