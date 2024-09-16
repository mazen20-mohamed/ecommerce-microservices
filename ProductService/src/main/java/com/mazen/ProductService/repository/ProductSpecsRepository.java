package com.mazen.ProductService.repository;

import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductSpecs;
import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductSpecsRepository extends JpaRepository<ProductSpecs,Long> {

    @Query("select c from ProductSpecs c where c.colors = :colors And c.product = :product And c.size =:size")
    Optional<ProductSpecs> findByColorAndSize(Colors colors, Product product, Size size);

    @Query("select c from ProductSpecs c where c.colors = :colors And c.product = :product")
    List<ProductSpecs> findByColor(Colors colors, Product product);

}
