package org.example.orderservice.common;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class UpdateOrderStatusCommand {
    @TargetAggregateIdentifier
    private final String orderId;
    private final OrderStatus status;
}