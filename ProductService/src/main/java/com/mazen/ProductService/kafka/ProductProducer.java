package com.mazen.ProductService.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductProducer {
    private final KafkaTemplate<String, String> template;

    public void sendProductId(String productId){
        log.info("Sending product Id... {}",productId);

        Message<String> message = MessageBuilder
                .withPayload(productId)
                .setHeader(KafkaHeaders.TOPIC,"product-deletion")
                .build();

        template.send(message);
    }
}
