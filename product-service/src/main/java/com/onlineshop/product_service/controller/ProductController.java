package com.onlineshop.product_service.controller;

import com.onlineshop.product_service.dto.ProductRequest;
import com.onlineshop.product_service.dto.ProductResponse;
import com.onlineshop.product_service.service.ProductService;
import com.onlineshop.product_service.exception.ProductNotFoundException;
import com.onlineshop.product_service.exception.ProductCreationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            log.info("Creating product: {}", productRequest);
            ProductResponse createdProduct = productService.createProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (ProductCreationException e) {
            log.error("Product creation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try {
            List<ProductResponse> products = productService.getAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            log.error("Error fetching products: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
        try {
            log.info("Fetching product with ID: {}", productId);
            ProductResponse product = productService.getProductById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (ProductNotFoundException e) {
            log.error("Product not found with ID: {}", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error fetching product with ID: {}", productId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable String productId, @RequestBody ProductRequest productRequest) {
        try {
            log.info("Updating product with ID: {}", productId);
            ProductResponse updatedProduct = productService.updateProduct(productId, productRequest);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } catch (ProductNotFoundException e) {
            log.error("Product not found with ID: {}", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error updating product with ID: {}", productId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProductById(@PathVariable String productId) {
        try {
            log.info("Deleting product with ID: {}", productId);
            productService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ProductNotFoundException e) {
            log.error("Product not found with ID: {}", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error deleting product with ID: {}", productId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
