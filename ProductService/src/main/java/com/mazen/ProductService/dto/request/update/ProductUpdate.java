package com.mazen.ProductService.dto.request.update;

import com.mazen.ProductService.model.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdate {
    @NotEmpty
    private String title;
    @Min(value = 1)
    private double price;
    @NotEmpty
    private String description;
    @NotNull
    private ProductCategory productCategory;
    @Size(min = 1)
    private List<ProductSpecsUpdate> productSpecsUpdates;
    @Size(min=1)
    List<ProductImageUpdate> productImageUpdates;
}
