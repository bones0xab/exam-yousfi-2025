package org.example.analyticsservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class AnalyticsResult {
    private long count = 0;
    private double totalRevenue = 0.0;
}