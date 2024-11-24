package com.onlineshop.product_service.controller;

import com.onlineshop.product_service.dto.ProductRequest;
import com.onlineshop.product_service.dto.ProductResponse;
import com.onlineshop.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        log.info("Creating product: {}", productRequest);
        return productService.createProduct(productRequest);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProductById(@PathVariable String productId) {
        log.info("Fetching product with ID: {}", productId);
        return productService.getProductById(productId);
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProductById(@PathVariable String productId, @RequestBody ProductRequest productRequest) {
        log.info("Updating product with ID: {}", productId);
        return productService.updateProduct(productId, productRequest);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable String productId) {
        log.info("Deleting product with ID: {}", productId);
        productService.deleteProduct(productId);
    }

    
    @Operation(summary = "Fetch all product categories")
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getProductCategories() {
        log.info("Fetching all product categories");
        return productService.getAllCategories();
    }

    @Operation(summary = "Search for products by keyword")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProducts(@RequestParam String keyword) {
        log.info("Searching for products with keyword: {}", keyword);
        return productService.searchProducts(keyword);
    }
}
