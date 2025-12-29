package org.example.orderservice.query.web;

import lombok.Data;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.example.orderservice.common.CreateOrderCommand;
import org.example.orderservice.common.GetOrderQuery;
import org.example.orderservice.common.OrderLineItem;
import org.example.orderservice.query.entities.OrderEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public OrderController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public CompletableFuture<String> createOrder(@RequestBody OrderRequestDTO dto) {
        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand command = new CreateOrderCommand(
                orderId,
                new Date(),
                dto.getDeliveryAddress(),
                dto.getItems()
        );

        return commandGateway.send(command);
    }

    @GetMapping("/{id}")
    public CompletableFuture<OrderEntity> getOrder(@PathVariable String id) {
        return queryGateway.query(
                new GetOrderQuery(id),
                ResponseTypes.instanceOf(OrderEntity.class)
        );
    }

    // DTO for incoming JSON
    @Data
    static class OrderRequestDTO {
        private String deliveryAddress;
        private List<OrderLineItem> items;
    }
}