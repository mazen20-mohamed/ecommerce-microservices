package com.mazen.ProductService.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_category")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String category;


    @OneToMany(mappedBy = "productCategory")
    private List<Product> products;
}

//Phones,
//Computers,
//SmartWatch,
//Camera,
//HeadPhones,
//Gaming,
//Clothes