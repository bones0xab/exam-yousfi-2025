package org.example.inventoryservice.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.inventoryservice.common.*;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String id;
    private int stock;
    private ProductState state;

    public ProductAggregate() {
        // Required by Axon
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand cmd) {
        // Business Logic: Validate inputs if necessary
        if (cmd.getPrice() <= 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        // Publish Event
        AggregateLifecycle.apply(new ProductCreatedEvent(
                        cmd.getId(),
                        cmd.getName(),
                        cmd.getPrice(),
                        cmd.getQuantity(),
                        ProductState.DISPONIBLE, // Default state [cite: 8]
                cmd.getCategoryId()
        ));
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.id = event.getId();
        this.stock = event.getQuantity();
        this.state = event.getState();
    }

    @CommandHandler
    public void handle(UpdateStockCommand cmd) {
        // Calculate new stock
        int newStock = this.stock + cmd.getQuantity();

        if (newStock < 0) {
            throw new IllegalStateException("Stock cannot be negative");
        }

        // Determine state based on stock
        ProductState newState = (newStock == 0) ? ProductState.RUPTURE : ProductState.DISPONIBLE;

        AggregateLifecycle.apply(new StockUpdatedEvent(
                this.id,
                newStock,
                newState
        ));
    }

    @EventSourcingHandler
    public void on(StockUpdatedEvent event) {
        this.stock = event.getNewQuantity();
        this.state = event.getNewState();
    }
}