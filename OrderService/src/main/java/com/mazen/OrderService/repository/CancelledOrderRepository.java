package com.mazen.OrderService.repository;

import com.mazen.OrderService.model.order.CanceledOrder;
import com.mazen.OrderService.model.order.FinishedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CancelledOrderRepository extends JpaRepository<CanceledOrder,String> {
    @Query("Select o from CanceledOrder o Where o.user_id = :user_id")
    Optional<List<CanceledOrder>> getOrdersByUserId(String user_id);
}
