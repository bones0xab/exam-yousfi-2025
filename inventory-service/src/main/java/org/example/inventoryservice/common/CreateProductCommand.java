package org.example.inventoryservice.common;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class CreateProductCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final String name;
    private final double price;
    private final int quantity;
    private final String categoryId; // Link to category
}