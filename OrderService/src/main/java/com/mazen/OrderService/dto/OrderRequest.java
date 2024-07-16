package com.mazen.OrderService.dto;


import com.mazen.OrderService.model.PaymentType;
import com.mazen.OrderService.model.ProductItem;
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
public class OrderRequest {
    private String user_id;
    private double totalPrice;
    private List<ProductRequest> productItems;
    private DetailsRequest detailsRequest;
    private PaymentType paymentType;
}
