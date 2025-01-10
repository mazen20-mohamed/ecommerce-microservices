package com.mazen.OrderService.controller;

import com.mazen.OrderService.dto.OrderRequest;
import com.mazen.OrderService.dto.OrderResponse;
import com.mazen.OrderService.dto.PagedResponse;
import com.mazen.OrderService.model.OrderStatus;
import com.mazen.OrderService.security.extractUserId.ExtractUserId;
import com.mazen.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public void createOrder(@RequestBody OrderRequest orderRequest, @ExtractUserId String userId,
                            @RequestHeader("Authorization") String authorization){
        orderRequest.setUserId(userId);
        orderService.createOrder(orderRequest,authorization);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id
            ,@RequestParam(required = false) OrderStatus orderStatus){
        if(orderStatus == (null)){
            return ResponseEntity.ok(orderService.getOrderById(id));
        }
        return ResponseEntity.ok(orderService.getOrderByIdAndStatus(id,orderStatus));
    }

    @GetMapping("/{page}/{size}")
    public PagedResponse<OrderResponse> getAllOrders(@PathVariable int page, @PathVariable int size,
                                                     @RequestParam(required = false) OrderStatus status){
        if(status == (null)){
            return orderService.getAllOrders(page,size);
        }
        return orderService.getAllOrderByStatus(page,size,status);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrderByUserId(@PathVariable String userId){
        return ResponseEntity.ok(orderService.getOrderByUserId(userId));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void changeStatusOfOrder(@RequestParam OrderStatus status
            ,@PathVariable String id){
        orderService.changeStatusOfOrder(status,id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(@PathVariable String id){
        orderService.deleteOrder(id);
    }

    @GetMapping("product/{productId}")
    public List<OrderResponse> getOrdersByProductId(@PathVariable String productId ,
                                                    @RequestParam(required = false) List<OrderStatus> orderStatus){
        return orderService.getOrdersByProductId(productId,orderStatus);
    }

}