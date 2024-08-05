package com.example.PhoneService.service;


import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SMSService {
    private final VonageClient vonageClient;

    @Value("${nexmo.from-number}")
    private String fromNumber;


    public void sendSMS(String toNumber, String message) {
        TextMessage textMessage = new TextMessage(fromNumber, toNumber, message);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully.");
        } else {
            log.error("Message failed with error: " +
                    response.getMessages().get(0).getErrorText());
        }
    }
}
