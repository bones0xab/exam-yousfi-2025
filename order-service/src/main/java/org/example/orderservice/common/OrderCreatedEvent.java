package org.example.orderservice.common;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderCreatedEvent {
    private final String orderId;
    private final Date orderDate;
    private final String deliveryAddress;
    private final List<OrderLineItem> items;
    private final OrderStatus status; // Initial status: CREATED
}