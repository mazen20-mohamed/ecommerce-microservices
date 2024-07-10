package com.mazen.ProductService.dto;


import com.mazen.ProductService.util.Colors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorRequest {
    private Colors colors;
    private int numberInStock;
}
