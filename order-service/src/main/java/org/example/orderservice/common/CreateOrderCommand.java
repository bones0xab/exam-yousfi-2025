package org.example.orderservice.common;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;
import java.util.List;

@Data
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    private final String orderId;
    private final Date orderDate;
    private final String deliveryAddress;
    private final List<OrderLineItem> items;
}