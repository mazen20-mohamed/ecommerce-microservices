package com.mazen.ProductService.repository;

import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductImage;
import com.mazen.ProductService.model.ProductSpecs;
import com.mazen.ProductService.util.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    @Query("select i from ProductImage i where i.colors = :colors And i.product = :product")
    List<ProductImage> findByColor(Colors colors, Product product);
}
