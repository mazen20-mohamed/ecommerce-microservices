package com.mazen.ProductService.repository;

import com.mazen.ProductService.model.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {
}
