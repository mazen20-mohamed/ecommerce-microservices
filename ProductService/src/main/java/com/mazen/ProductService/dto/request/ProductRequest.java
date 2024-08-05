package com.mazen.ProductService.dto.request;

import com.mazen.ProductService.model.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    @NotEmpty
    private String description;
    @NotNull
    private ProductCategory productCategory;
    private List<ProductColorRequest> productColorRequests;
    @Size(min = 1,message = "you must put images for product")
    private List<MultipartFile> images;
}
