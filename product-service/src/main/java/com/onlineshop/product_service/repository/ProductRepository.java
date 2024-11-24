package com.onlineshop.product_service.repository;

import com.onlineshop.product_service.model.Product;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String nameKeyword, String descriptionKeyword);

    @Aggregation("{ '$group': { '_id': '$category' } }")
    List<String> findDistinctCategories();
}
