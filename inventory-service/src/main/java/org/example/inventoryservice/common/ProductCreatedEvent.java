package org.example.inventoryservice.common;

import lombok.Data;

@Data
public class ProductCreatedEvent {
    private final String id;
    private final String name;
    private final double price;
    private final int quantity;
    private final ProductState state; // Default state
    private final String categoryId;
}