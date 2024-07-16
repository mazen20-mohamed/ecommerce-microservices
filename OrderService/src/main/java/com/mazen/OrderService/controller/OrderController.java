package com.mazen.OrderService.controller;

import com.mazen.OrderService.dto.OrderRequest;
import com.mazen.OrderService.dto.OrderResponse;
import com.mazen.OrderService.model.Status;
import com.mazen.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public void setOrder(@RequestBody OrderRequest orderRequest){
        orderService.createOrder(orderRequest);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PatchMapping("/{orderId}")
    public void changeStatusOfOrder(@RequestParam Status status ,@PathVariable String orderId){
        orderService.changeStatusOfOrder(status,orderId);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable String orderId){
        orderService.deleteOrder(orderId);
    }


    @GetMapping("/all/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrderByUserId(@PathVariable String userId){
        return ResponseEntity.ok(orderService.getOrderByUserId(userId));
    }


}
