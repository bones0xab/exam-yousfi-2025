package org.example.analyticsservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class OrderAnalyticsDTO {
    private String orderId;
    private double price;
}