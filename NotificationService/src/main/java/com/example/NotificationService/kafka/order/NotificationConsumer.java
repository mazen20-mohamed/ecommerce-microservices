package com.example.NotificationService.kafka.order;


import com.example.NotificationService.kafka.message.PhoneMessageProducer;
import com.example.NotificationService.kafka.message.PhoneMessage;
import com.example.NotificationService.model.Notification;
import com.example.NotificationService.model.NotificationType;
import com.example.NotificationService.repository.NotificationRepository;
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
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final PhoneMessageProducer phoneMessageProducer;

    @KafkaListener(topics = "order-topic",groupId = "orderGroup")
    public void consumeOrderNotifications(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(format("Consuming notification from order service :: %s",orderConfirmation));
        Notification notification = notificationRepository.save(
                Notification.builder()
                        .notificationType(NotificationType.ORDER)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );
        String message = "Order with reference id " + orderConfirmation.getOrderReference() +
                " has been " + orderConfirmation.getStatus() +
                " to user with phone number " +
                orderConfirmation.getDetailsShippingResponse().getPhoneNumber();

        phoneMessageProducer.sendMessageNotification(
                PhoneMessage.builder()
                        .toNumber(orderConfirmation.getDetailsShippingResponse().getPhoneNumber())
                        .message(message)
                        .build()
        );
        log.info("Saved Notification : {}",notification);
    }
}
