package org.example.orderservice.common;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class UpdateStockCommand {
    @TargetAggregateIdentifier
    private final String id; // Product ID
    private final int quantity; // Negative to deduct
}