package org.example.inventoryservice.common;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class UpdateStockCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final int quantity; // The quantity to add or subtract
}