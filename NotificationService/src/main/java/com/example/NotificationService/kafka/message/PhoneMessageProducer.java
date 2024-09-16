package com.example.NotificationService.kafka.message;


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
public class PhoneMessageProducer {
    private final KafkaTemplate<String, PhoneMessage> template;

    public void sendMessageNotification(PhoneMessage phoneNumber){
        log.info("Sending message from notification to phone service");

        Message<PhoneMessage> message = MessageBuilder
                .withPayload(phoneNumber)
                .setHeader(KafkaHeaders.TOPIC,"message-topic")
                .build();
        template.send(message);
    }
}
