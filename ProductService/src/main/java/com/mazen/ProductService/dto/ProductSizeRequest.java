package com.mazen.ProductService.dto;

import com.mazen.ProductService.util.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSizeRequest {

    private Size size;
    @jakarta.validation.constraints.Size(min = 1,max = 8)
    private List<ProductColorRequest> productColorRequests;
}
