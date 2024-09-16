package com.mazen.ProductService.model;


import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_specs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductSpecs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(value = EnumType.STRING)
    private Colors colors;

   private Size size;

   private int numberInStock;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;

}
