package com.mazen.ProductService.dto;


import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorResponse {
    private long id;
    private Colors colors;
    private int numberInStock;
    private Size size;
}
