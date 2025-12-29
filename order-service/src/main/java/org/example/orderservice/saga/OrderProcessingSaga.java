package org.example.orderservice.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.example.orderservice.common.*;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderCreatedEvent event) {
        // 1. Order is created (PENDING).
        // 2. Loop through items and reserve stock in Inventory Service.
        // NOTE: For simplicity, we assume 1 item or handle the first item.
        // In a real exam complex scenario, you might loop or use a loop of commands.
        // Here we demonstrate the logic for the first item found.

        if (event.getItems().isEmpty()) {
            return; // Or cancel immediately
        }

        OrderLineItem item = event.getItems().get(0);

        // Prepare command for Inventory Service
        // We deduct stock (negative value)
        UpdateStockCommand stockCmd = new UpdateStockCommand(
                item.getProductId(),
                -item.getQuantity()
        );

        // 3. Send Command and wait for result (Compensation Logic)
        commandGateway.send(stockCmd, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // FAILURE: Stock insufficient or Product not found
                // COMPENSATION: Cancel the Order
                Throwable e = commandResultMessage.optionalExceptionResult().orElse(new RuntimeException("Unknown error"));
                System.out.println("Stock reservation failed: " + e.getMessage());

                commandGateway.send(new UpdateOrderStatusCommand(
                        event.getOrderId(),
                        OrderStatus.CANCELED
                ));
            } else {
                // SUCCESS: Stock reserved
                // ACTION: Activate the Order
                commandGateway.send(new UpdateOrderStatusCommand(
                        event.getOrderId(),
                        OrderStatus.ACTIVATED
                ));
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga // Ends the Saga lifecycle
    public void on(OrderActivatedEvent event) {
        System.out.println("Order " + event.getOrderId() + " is fully ACTIVATED.");
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void on(OrderCancelledEvent event) {
        System.out.println("Order " + event.getOrderId() + " was CANCELED.");
    }
}