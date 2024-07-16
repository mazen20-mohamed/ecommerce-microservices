package com.mazen.OrderService.dto;

import com.mazen.OrderService.model.PaymentType;
import com.mazen.OrderService.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderResponse {
    private String orderId;
    private PaymentType paymentType;
    private Status status;
    private long detailsId;
    private double totalPrice;
    private List<Long> productItemsId;
    private String reasonOfCancel;
}
