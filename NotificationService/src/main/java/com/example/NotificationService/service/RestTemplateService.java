package com.example.NotificationService.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestTemplateService {
    private final RestTemplate restTemplate;

    public void sendMessageToPhoneService(String number,String message){
        String url = "http://localhost:8555/v1/sms/send";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("toNumber", number)
                .queryParam("message", message);
        ResponseEntity<String> response =
                restTemplate.postForEntity(uriBuilder.toUriString(),
                        null, String.class);
        log.info(response.getBody());
    }

}
