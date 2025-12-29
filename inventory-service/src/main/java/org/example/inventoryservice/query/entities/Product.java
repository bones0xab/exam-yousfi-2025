package org.example.inventoryservice.query.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.inventoryservice.common.ProductState;

@Entity
@Data @AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    private String name;
    private double price;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private ProductState state;

    // Simplification: In a real scenario, this might be a @ManyToOne relationship
    private String categoryId;
}

