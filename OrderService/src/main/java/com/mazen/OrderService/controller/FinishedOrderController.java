package com.mazen.OrderService.controller;

import com.mazen.OrderService.dto.OrderResponse;
import com.mazen.OrderService.dto.PagedResponse;
import com.mazen.OrderService.service.FinishedOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/finishedOrder")
@RequiredArgsConstructor
public class FinishedOrderController {
    private final FinishedOrderService finishedOrderService;

    @PostMapping("/{orderId}")
    public void changeOrderToFinished(@PathVariable String orderId){
        finishedOrderService.changeOrderToFinished(orderId);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getFinishedOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(finishedOrderService.getFinishedOrderById(orderId));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<OrderResponse>> getFinishedOrderByUserId(@PathVariable String userId){
        return ResponseEntity.ok(finishedOrderService.getOrderByUserId(userId));
    }

    @GetMapping("/all/{page}/{size}")
    public ResponseEntity<PagedResponse<OrderResponse>> getAllFinishedOrders(@PathVariable int page, @PathVariable int size){
        return ResponseEntity.ok(finishedOrderService.getAllFinishedOrders(page,size));
    }


}
