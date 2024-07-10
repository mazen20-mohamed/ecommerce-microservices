package com.mazen.ProductService.repository;


import com.mazen.ProductService.model.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSizeRepository extends JpaRepository<ProductSize,Long> {
}
