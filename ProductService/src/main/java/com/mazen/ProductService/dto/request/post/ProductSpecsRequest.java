package com.mazen.ProductService.dto.request.post;


import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecsRequest {
    @NotEmpty
    private Colors colors;
    private Size size;
    @Min(0)
    private int numberInStock;
}
