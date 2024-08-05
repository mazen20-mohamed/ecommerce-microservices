package com.example.SaleService.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flash_sale")
public class FlashSale extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDateTime startDateFlashSale;

    private LocalDateTime endDateFlashSale;

    @OneToMany(mappedBy = "flashSale")
    private List<ProductSale> productSales;
}
