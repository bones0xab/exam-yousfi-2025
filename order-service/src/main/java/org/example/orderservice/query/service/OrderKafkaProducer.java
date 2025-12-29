package org.example.orderservice.query.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.eventhandling.EventHandler;
import org.example.orderservice.common.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaProducer {

    private final KafkaTemplate<String, OrderAnalyticsDTO> kafkaTemplate;

    public OrderKafkaProducer(KafkaTemplate<String, OrderAnalyticsDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        // Calculate total price for this specific order
        double totalOrderPrice = event.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        // Create a simple DTO for Analytics
        OrderAnalyticsDTO analyticsData = new OrderAnalyticsDTO(
                event.getOrderId(),
                totalOrderPrice
        );

        // Send to Kafka Topic "orders-topic"
        kafkaTemplate.send("orders-topic", event.getOrderId(), analyticsData);
    }

    // Simple DTO inner class (or separate file)
    @Data
    @AllArgsConstructor
    public static class OrderAnalyticsDTO {
        private String orderId;
        private double price;
    }
}