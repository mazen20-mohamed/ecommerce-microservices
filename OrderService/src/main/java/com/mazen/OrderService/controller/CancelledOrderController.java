package com.mazen.OrderService.controller;

import com.mazen.OrderService.dto.CancelOrderResponse;
import com.mazen.OrderService.service.CancelledOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/cancelOrder")
@RequiredArgsConstructor
public class CancelledOrderController {
    private final CancelledOrderService cancelledOrderService;

    @PostMapping("/{orderId}")
    public void changeOrderToCancel(@PathVariable String orderId,@RequestParam String reason){
        cancelledOrderService.changeOrderToCancel(orderId,reason);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<CancelOrderResponse> getCancelledOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(cancelledOrderService.getCancelledOrderById(orderId));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<CancelOrderResponse>> getFinishedOrderByUserId(@PathVariable String userId){
        return ResponseEntity.ok(cancelledOrderService.getOrderByUserId(userId));
    }
}
