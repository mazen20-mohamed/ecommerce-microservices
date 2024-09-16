package com.mazen.ProductService.dto.request.update;

import com.mazen.ProductService.util.Colors;
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
public class ProductImageUpdate {
    @NotNull
    private Colors colors;
    private List<MultipartFile> images;
    private List<String> imagesUrl;
}
