package com.mazen.ProductService.model;

import com.mazen.ProductService.util.Size;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product_sizes")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(value = EnumType.STRING)
    private Size size;

    @OneToMany(mappedBy = "productSize",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ProductColor> productColors;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id",nullable = false)
    private Product product;
}
