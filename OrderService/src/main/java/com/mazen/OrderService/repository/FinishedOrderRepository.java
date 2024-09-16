package com.mazen.OrderService.repository;

import com.mazen.OrderService.model.order.FinishedOrder;
import com.mazen.OrderService.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FinishedOrderRepository extends JpaRepository<FinishedOrder,String> {
    @Query("Select o from FinishedOrder o Where o.user_id = :user_id")
    Optional<List<FinishedOrder>> getOrdersByUserId(String user_id);


    @Override
    Page<FinishedOrder> findAll(Pageable pageable);
}
