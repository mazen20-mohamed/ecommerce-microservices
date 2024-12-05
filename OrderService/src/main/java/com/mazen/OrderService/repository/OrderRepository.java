package com.mazen.OrderService.repository;
import com.mazen.OrderService.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
