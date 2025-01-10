package com.mazen.OrderService.kafka.consumer;

import com.mazen.OrderService.dto.OrderResponse;
import com.mazen.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProductConsumer {
    private final OrderService orderService;

    @KafkaListener(topics = "product-deletion",groupId = "productGroup")
    public void consumeProductDeletion(String id) throws MessagingException {
        log.info("Consuming product deletion from product service with id :: {}",id);
        // delete orders of product...
        List<OrderResponse> orderResponses = orderService.getOrdersByProductId(id, List.of());
        if(!orderResponses.isEmpty()){
            // loop over all orders with product id
            orderResponses.forEach(orderResponse ->
                    orderService.deleteOrder(orderResponse.getOrderId()));
        }
    }
}
