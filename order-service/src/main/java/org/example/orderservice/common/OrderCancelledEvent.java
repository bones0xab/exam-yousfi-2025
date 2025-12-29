package org.example.orderservice.common;

import lombok.Data;

@Data
public class OrderCancelledEvent {
    private final String orderId;
    private final OrderStatus status;
}