package org.example.orderservice.query.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.orderservice.common.OrderStatus;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders") // 'order' is a reserved keyword in SQL
public class OrderEntity {
    @Id
    private String id;
    private Date orderDate;
    private Date deliveryDate; // Nullable until shipped
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderLineEntity> items;
}
