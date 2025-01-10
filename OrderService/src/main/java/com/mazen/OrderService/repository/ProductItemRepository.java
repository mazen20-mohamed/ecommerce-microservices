package com.mazen.OrderService.repository;

import com.mazen.OrderService.model.order.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem,Long> {
}
