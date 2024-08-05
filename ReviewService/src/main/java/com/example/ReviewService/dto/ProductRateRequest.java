package com.example.ReviewService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRateRequest {
    private String product_id;
    private String user_id;
    private double rate;
    private String comment;
}
