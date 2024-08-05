package com.mazen.ProductService.model;


import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_color")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(value = EnumType.STRING)
    private Colors colors;

    private int numberInStock;

    private Size size;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id",referencedColumnName = "id",nullable = false)
    private Product product;

}
