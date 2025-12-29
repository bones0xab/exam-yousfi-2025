package org.example.inventoryservice.web;

import lombok.Data;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.example.inventoryservice.common.CreateProductCommand;
import org.example.inventoryservice.common.GetAllProductsQuery;
import org.example.inventoryservice.common.GetProductQuery;
import org.example.inventoryservice.common.UpdateStockCommand;
import org.example.inventoryservice.query.entities.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
public class InventoryController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public InventoryController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public CompletableFuture<String> createProduct(@RequestBody CreateProductDTO dto) {
        String id = UUID.randomUUID().toString();
        CreateProductCommand command = new CreateProductCommand(
                id, dto.getName(), dto.getPrice(), dto.getQuantity(), dto.getCategoryId()
        );
        return commandGateway.send(command);
    }

    @PutMapping("/{id}/stock")
    public CompletableFuture<String> updateStock(@PathVariable String id, @RequestBody int quantity) {
        return commandGateway.send(new UpdateStockCommand(id, quantity));
    }

    @GetMapping("/{id}")
    public CompletableFuture<Product> getProduct(@PathVariable String id) {
        return queryGateway.query(
                new GetProductQuery(id),
                ResponseTypes.instanceOf(Product.class)
        );
    }

    @GetMapping
    public CompletableFuture<List<Product>> getAllProducts() {
        return queryGateway.query(
                new GetAllProductsQuery(),
                ResponseTypes.multipleInstancesOf(Product.class)
        );
    }

    // Simple DTO for API request body
    @Data
    static class CreateProductDTO {
        private String name;
        private double price;
        private int quantity;
        private String categoryId;
    }
}