package com.mazen.ProductService.dto;

import com.mazen.ProductService.model.ProductCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotEmpty
    private String title;
    @Min(value = 1)
    private double price;
    private String description;
    private ProductCategory productCategory;
    private List<ProductSizeRequest> productSizeRequests;
    private List<MultipartFile> images;
}
