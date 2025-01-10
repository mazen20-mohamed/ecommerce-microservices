package com.mazen.OrderService.repository;

import com.mazen.OrderService.model.order.BillingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingDetailsRepository extends JpaRepository<BillingDetails,Long> {
}
