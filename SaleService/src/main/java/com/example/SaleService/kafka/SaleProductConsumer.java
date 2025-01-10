package com.example.SaleService.kafka;


import com.example.SaleService.service.ProductSaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleProductConsumer {

    private final ProductSaleService productSaleService;

    @KafkaListener(topics = "product-deletion",groupId = "productGroup")
    public void consumeProductDeletion(String id) throws MessagingException {
        log.info("Consuming product deletion from product service with id :: {}",id);
        // delete sale of product...
        productSaleService.deleteProduct(id);
    }
}
