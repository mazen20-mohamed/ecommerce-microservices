package com.mazen.OrderService.repository;

import com.mazen.OrderService.model.BillingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BillingDetailsRepository extends JpaRepository<BillingDetails,Long> {
}
