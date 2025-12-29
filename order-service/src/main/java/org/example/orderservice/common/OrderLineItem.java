package org.example.orderservice.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderLineItem {
    private final String productId;
    private final int quantity;
    private final double unitPrice;
    private final double discount;
}
