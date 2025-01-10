package com.mazen.FileStorage.kafka;


import com.mazen.FileStorage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileProductConsumer {
    private final FileStorageService fileStorageService;

    @KafkaListener(topics = "product-deletion",groupId = "productGroup")
    public void consumeProductDeletion(String id) throws MessagingException {
        log.info("Consuming product deletion from product service with id :: {}",id);
        // delete folder of product...
        fileStorageService.deletePhotosOfProduct(id);
    }
}
