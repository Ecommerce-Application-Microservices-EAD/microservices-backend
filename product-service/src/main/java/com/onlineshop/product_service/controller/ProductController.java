package com.onlineshop.product_service.controller;

import com.onlineshop.product_service.dto.ProductRequest;
import com.onlineshop.product_service.dto.ProductResponse;
import com.onlineshop.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;



@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile image) throws IOException {

        log.info("Creating product: {}", productJson);

        ProductRequest productRequest = objectMapper.readValue(productJson, ProductRequest.class);
        byte[] imageData = image.getBytes();

        return productService.createProduct(productRequest, imageData);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProductById(@PathVariable String productId) {
        log.info("Fetching product with ID: {}", productId);
        return productService.getProductById(productId);
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProductById(
            @PathVariable String productId,
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        log.info("Updating product with ID: {}", productId);

        ProductRequest productRequest = objectMapper.readValue(productJson, ProductRequest.class);
        byte[] imageData = (image != null) ? image.getBytes() : null;

        return productService.updateProduct(productId, productRequest, imageData);
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

    @PatchMapping("/{productId}/reduce-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse reduceProductQuantity(
            @PathVariable String productId,
            @RequestParam int count) {
        log.info("Reducing quantity of product with ID: {} by count: {}", productId, count);
        return productService.reduceProductQuantity(productId, count);
    }
}
