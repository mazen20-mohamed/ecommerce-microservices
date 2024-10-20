package com.mazen.ProductService.repository;

import com.mazen.ProductService.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
    Optional<ProductCategory> findByCategory(String category);
}
