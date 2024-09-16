package com.mazen.ProductService.model;


import com.mazen.ProductService.util.Colors;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Colors colors;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ElementCollection
    @CollectionTable(name = "images_product",
            joinColumns = @JoinColumn(name = "product_Image_id"))
    @Column(name = "images_url")
    private List<String> imagesPaths = new ArrayList<>();
}
