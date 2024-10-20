package com.mazen.ProductService.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private double price;

    private String description;

    @ManyToOne
    @JoinColumn(name = "product_category_id",referencedColumnName = "id")
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductSpecs> productSpecs;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductImage> productImages;

}
