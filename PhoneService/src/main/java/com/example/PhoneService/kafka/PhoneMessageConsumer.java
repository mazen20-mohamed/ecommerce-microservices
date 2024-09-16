package com.example.PhoneService.kafka;


import com.example.PhoneService.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhoneMessageConsumer {
    private final SMSService smsService;
    @KafkaListener(topics = "message-topic",groupId = "messageGroup")
    public void consumeOrderNotifications(PhoneMessage phoneMessage) throws MessagingException{
        log.info("Receive message to send for user's phone Number"+phoneMessage.getToNumber());

        smsService.sendSMS(phoneMessage.getToNumber(),phoneMessage.getMessage());

        log.info("Message has sent to user's phone number "+ phoneMessage.getToNumber());
    }
}
