package com.example.NotificationService.kafka.order;


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
    private String user_id;
    private OrderStatus status;
    private DetailsShippingResponse detailsShippingResponse;
}
