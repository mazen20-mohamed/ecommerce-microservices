package com.mazen.ProductService.dto;


import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecsResponse {
    private Colors colors;
    private List<SpecsDetails> details;
    private List<String> imagesUrl;
}
