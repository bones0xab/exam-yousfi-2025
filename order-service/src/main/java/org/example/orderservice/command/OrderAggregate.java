package org.example.orderservice.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.orderservice.common.*;

import java.util.List;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private OrderStatus status;
    // We keep items in state mainly if business logic depends on them later
    private List<OrderLineItem> items;

    public OrderAggregate() {}

    @CommandHandler
    public OrderAggregate(CreateOrderCommand cmd) {
        // Business Rule: Start as CREATED
        AggregateLifecycle.apply(new OrderCreatedEvent(
                cmd.getOrderId(),
                cmd.getOrderDate(),
                cmd.getDeliveryAddress(),
                cmd.getItems(),
                OrderStatus.CREATED
        ));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.items = event.getItems();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(UpdateOrderStatusCommand cmd) {
        if (this.status == OrderStatus.CANCELED || this.status == OrderStatus.DELIVERED) {
            // Simple validation rule
            return;
        }

        if (cmd.getStatus() == OrderStatus.ACTIVATED) {
            AggregateLifecycle.apply(new OrderActivatedEvent(this.orderId, OrderStatus.ACTIVATED));
        } else if (cmd.getStatus() == OrderStatus.CANCELED) {
            AggregateLifecycle.apply(new OrderCancelledEvent(this.orderId, OrderStatus.CANCELED));
        }
    }

    @EventSourcingHandler
    public void on(OrderActivatedEvent event) {
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(OrderCancelledEvent event) {
        this.status = event.getStatus();
    }
}