package com.mazen.OrderService.repository;

import com.mazen.OrderService.model.OrderStatus;
import com.mazen.OrderService.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {
    @Query("Select o from Order o Where o.user_id = :user_id")
    Optional<List<Order>> getOrdersByUserId(String user_id);


    @Override
    Page<Order> findAll(Pageable pageable);


    @Query("Select o from Order o Where o.status = :status")
    Page<Order> findAllByStatus(Pageable pageable, OrderStatus status);
}
