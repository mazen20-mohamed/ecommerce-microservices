package com.example.NotificationService.kafka.message;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaMessageTopicConfig {
    @Bean
    public NewTopic MessageTopic(){
        return TopicBuilder
                .name("message-topic")
                .build();
    }

}
