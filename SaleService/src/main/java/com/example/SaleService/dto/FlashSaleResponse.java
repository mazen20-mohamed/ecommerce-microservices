package com.example.SaleService.dto;


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
public class FlashSaleResponse {
    private long id;
    private LocalDateTime startDateFlashSale;
    private LocalDateTime endDateFlashSale;
    private List<ProductSaleDTO> productSaleDTOS;
}
