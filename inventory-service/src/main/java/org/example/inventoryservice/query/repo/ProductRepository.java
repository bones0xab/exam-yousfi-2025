package org.example.inventoryservice.query.repo;

import org.example.inventoryservice.query.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
