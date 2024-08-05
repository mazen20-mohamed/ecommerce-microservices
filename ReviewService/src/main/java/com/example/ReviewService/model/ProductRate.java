package com.example.ReviewService.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_rate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRate {
    @Id
    private String product_id;

    private double rate;

    private long numberOfRates;
}
