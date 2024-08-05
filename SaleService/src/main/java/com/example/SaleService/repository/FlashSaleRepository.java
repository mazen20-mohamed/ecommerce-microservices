package com.example.SaleService.repository;

import com.example.SaleService.model.FlashSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FlashSaleRepository extends JpaRepository<FlashSale,Long> {


    @Query("SELECT f FROM FlashSale f WHERE :current BETWEEN f.startDateFlashSale AND f.endDateFlashSale")
    Optional<FlashSale> getFlashSaleBetweenStartAndEnd(LocalDateTime current);


    @Override
    Page<FlashSale> findAll(Pageable pageable);
}
