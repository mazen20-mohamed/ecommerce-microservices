package com.mazen.ProductService.dto.request.post;


import com.mazen.ProductService.util.Colors;
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
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageRequest {
    @NotNull
    private Colors colors;
    @NotEmpty
    private List<MultipartFile> images;
}
