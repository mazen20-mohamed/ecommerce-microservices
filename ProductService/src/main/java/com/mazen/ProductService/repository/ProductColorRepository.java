package com.mazen.ProductService.repository;

import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductColor;
import com.mazen.ProductService.model.ProductSize;
import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {
    @Query("Select c from ProductColor c Where c.productSize = :product And c.colors = :colors")
    Optional<ProductColor> findByProductSizeAndColor(ProductSize product, Colors colors);
}
