package com.mazen.ProductService.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProductTopicConfig {

    @Bean
    public NewTopic ProductTopic(){
        return TopicBuilder
                .name("product-deletion")
                .build();
    }

}
