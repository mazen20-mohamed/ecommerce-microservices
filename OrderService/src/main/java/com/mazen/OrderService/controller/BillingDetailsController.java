package com.mazen.OrderService.controller;

import com.mazen.OrderService.dto.DetailsShippingResponse;
import com.mazen.OrderService.service.BillingDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1/details")
@RestController
@RequiredArgsConstructor
public class BillingDetailsController {
    private final BillingDetailsService billingDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<DetailsShippingResponse> getShippingDetailsById(@PathVariable long id){
        return ResponseEntity.ok(billingDetailsService.getShippingDetailsById(id));
    }

}
