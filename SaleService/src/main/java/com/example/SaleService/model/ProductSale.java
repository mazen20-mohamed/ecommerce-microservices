package com.example.SaleService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_sale")
public class ProductSale {
    @Id
    private String product_id;

    private int discountPercent;

    @ManyToOne
    @JoinColumn(name = "flash_sale_id",referencedColumnName = "id")
    private FlashSale flashSale;
}
