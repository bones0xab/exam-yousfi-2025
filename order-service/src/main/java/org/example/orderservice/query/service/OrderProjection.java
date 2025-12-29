package org.example.orderservice.query.service;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.example.orderservice.common.GetOrderQuery;
import org.example.orderservice.common.OrderActivatedEvent;
import org.example.orderservice.common.OrderCancelledEvent;
import org.example.orderservice.common.OrderCreatedEvent;
import org.example.orderservice.query.entities.OrderEntity;
import org.example.orderservice.query.entities.OrderLineEntity;
import org.example.orderservice.query.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderProjection {

    private final OrderRepository orderRepository;

    public OrderProjection(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        OrderEntity order = new OrderEntity();
        order.setId(event.getOrderId());
        order.setOrderDate(event.getOrderDate());
        order.setDeliveryAddress(event.getDeliveryAddress());
        order.setStatus(event.getStatus());

        List<OrderLineEntity> lines = new ArrayList<>();
        if (event.getItems() != null) {
            lines = event.getItems().stream().map(item -> {
                OrderLineEntity line = new OrderLineEntity();
                line.setProductId(item.getProductId());
                line.setQuantity(item.getQuantity());
                line.setUnitPrice(item.getUnitPrice());
                line.setDiscount(item.getDiscount());
                line.setOrder(order);
                return line;
            }).collect(Collectors.toList());
        }
        order.setItems(lines);

        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderActivatedEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(event.getStatus());
            orderRepository.save(order);
        });
    }

    @EventHandler
    public void on(OrderCancelledEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(event.getStatus());
            orderRepository.save(order);
        });
    }

    @QueryHandler
    public OrderEntity handle(GetOrderQuery query) {
        return orderRepository.findById(query.getOrderId()).orElse(null);
    }
}