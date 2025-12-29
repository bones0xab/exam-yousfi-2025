package org.example.inventoryservice.common;

import lombok.Data;

@Data
public class StockUpdatedEvent {
    private final String id;
    private final int newQuantity;
    private final ProductState newState;
}