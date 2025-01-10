package com.mazen.OrderService.dto;


import com.mazen.OrderService.model.PaymentType;
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
    private String userId;
    private List<ProductRequest> productItems;
    private DetailsRequest detailsRequest;
    private PaymentType paymentType;
}
