package org.example.inventoryservice.query.service;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.example.inventoryservice.common.GetAllProductsQuery;
import org.example.inventoryservice.common.GetProductQuery;
import org.example.inventoryservice.common.ProductCreatedEvent;
import org.example.inventoryservice.common.StockUpdatedEvent;
import org.example.inventoryservice.query.entities.Product;
import org.example.inventoryservice.query.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryProjection {

    private final ProductRepository productRepository;

    public InventoryProjection(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- Write to Read DB (Event Handling) ---

    @EventHandler
    public void on(ProductCreatedEvent event) {
        Product product = new Product(
                event.getId(),
                event.getName(),
                event.getPrice(),
                event.getQuantity(),
                event.getState(),
                event.getCategoryId()
        );
        productRepository.save(product);
    }

    @EventHandler
    public void on(StockUpdatedEvent event) {
        Product product = productRepository.findById(event.getId()).orElse(null);
        if (product != null) {
            product.setQuantity(event.getNewQuantity());
            product.setState(event.getNewState());
            productRepository.save(product);
        }
    }

    // --- Read from Read DB (Query Handling) ---

    @QueryHandler
    public Product handle(GetProductQuery query) {
        return productRepository.findById(query.getId()).orElse(null);
    }

    @QueryHandler
    public List<Product> handle(GetAllProductsQuery query) {
        return productRepository.findAll();
    }
}