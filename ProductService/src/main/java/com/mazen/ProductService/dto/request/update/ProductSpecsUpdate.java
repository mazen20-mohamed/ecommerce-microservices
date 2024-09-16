package com.mazen.ProductService.dto.request.update;


import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecsUpdate {
    @NotNull
    private Colors colors;
    private Size size;
    @Min(0)
    private int numberInStock;
}
