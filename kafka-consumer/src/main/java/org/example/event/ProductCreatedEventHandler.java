package org.example.event;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.NonRetryableException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@KafkaListener(topics = "product-created-events-topic", groupId = "")
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    @KafkaHandler
    public void hadle(ProductCreatedEvent productCreatedEvent) {
        if (true) {
            throw new NonRetryableException("Non retryable exception");
        }


    }
}
