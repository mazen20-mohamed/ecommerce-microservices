package com.mazen.ProductService.repository;

import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,String> {
    @Query("select p from Product p where p.productCategory = :productCategory")
    Page<Product> getAllProductsByCategory(Pageable pageable, ProductCategory productCategory);

    @Override
    Page<Product> findAll(Pageable pageable);

    @Query("select p from Product p ORDER BY RAND()")
    Page<Product> getAllProductsRandom(Pageable pageable);
}
