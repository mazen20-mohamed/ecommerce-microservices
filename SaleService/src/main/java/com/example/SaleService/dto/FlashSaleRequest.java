package com.example.SaleService.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleRequest {
    @NotNull
    private LocalDateTime startDateFlashSale;
    @NotNull
    private LocalDateTime endDateFlashSale;
    @Size(min = 1)
    private List<ProductSaleDTO> productSaleDTOS;
}
