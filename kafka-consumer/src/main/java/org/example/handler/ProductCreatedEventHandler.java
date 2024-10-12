package org.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.event.ProductCreatedEvent;
import org.example.exception.NonRetryableException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        if (true)
            throw new NonRetryableException("Non retryable exception");
        log.info("Received event: {}", productCreatedEvent.getTitle());
    }
}
