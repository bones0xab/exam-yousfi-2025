package org.example.orderservice.common;

import lombok.Data;

@Data
public class GetOrderQuery {
    private final String orderId;
}
