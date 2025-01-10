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

    @Query("Select o from Order o where o.userId = :userId")
    Optional<List<Order>> getOrdersByUserId(String userId);

    @Query("Select o from Order o where o.userId = :userId and o.status = :status")
    Optional<Order> getOrdersByUserIdAndStatus(String userId,OrderStatus status);

    @Query("select o from Order o where o.status = :status")
    Page<Order> findAllByStatus(Pageable pageable, OrderStatus status);


    @Query("select o from Order o join o.productItems pi where pi.productId = :productId")
    Optional<List<Order>> getOrderByProductId(String productId);

    @Query("select o from Order o join o.productItems pi where pi.productId = :productId and o.status in (:statuses)")
    Optional<List<Order>> getOrderByProductIdAndStatus(String productId,List<OrderStatus> statuses);

}
