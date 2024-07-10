package com.mazen.ProductService.model;


import jakarta.persistence.*;
import lombok.*;

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

    @ElementCollection
    @CollectionTable(name = "images_product",
            joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "images_url")
    private List<String> imagesPaths;

    @Enumerated(value = EnumType.STRING)
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductSize> productSizes;
}
