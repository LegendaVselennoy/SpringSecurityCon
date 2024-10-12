package org.example.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductDTO;
import org.example.service.ProductService;
import org.example.service.event.ProductCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Override
    public String createProduct(ProductDTO product) {
        //TODO save DB
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                product.getTitle(),
                product.getPrice(),
                product.getQuantity());

//        try {
//            SendResult<String, ProductCreatedEvent> result =
//                    kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();
//            log.info("Topic: {}", result.getRecordMetadata().topic());
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        } синхронно , когда нужно дождаться ответа

        CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to message: {}", ex.getMessage());
            }else {
                log.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        }); // асинхронно

        log.info("Return: {}", productId);

        return productId;
    }
}
