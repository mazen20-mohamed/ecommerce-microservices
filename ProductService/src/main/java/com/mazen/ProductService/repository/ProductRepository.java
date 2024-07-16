package com.mazen.ProductService.repository;

import com.mazen.ProductService.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,String> {

}
