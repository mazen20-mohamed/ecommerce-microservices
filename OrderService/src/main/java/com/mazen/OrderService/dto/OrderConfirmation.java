package com.mazen.OrderService.dto;

import com.mazen.OrderService.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmation {
    private String orderReference;
    private double orderPrice;
    private OrderStatus status;
    private String user_id;
    private DetailsShippingResponse detailsShippingResponse;
}
