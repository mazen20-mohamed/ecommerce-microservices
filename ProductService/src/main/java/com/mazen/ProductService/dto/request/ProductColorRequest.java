package com.mazen.ProductService.dto.request;


import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorRequest {
    @NotEmpty
    private Colors colors;
    @Min(1)
    private int numberInStock;
    private Size size;
}
