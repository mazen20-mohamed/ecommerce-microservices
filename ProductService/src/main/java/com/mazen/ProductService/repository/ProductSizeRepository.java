package com.mazen.ProductService.repository;


import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductSize;
import com.mazen.ProductService.util.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductSizeRepository extends JpaRepository<ProductSize,Long> {
    @Query("Select s from ProductSize s Where s.product = :product And s.size = :size")
    Optional<ProductSize> findByProductAndSize(Product product, Size size);
}
