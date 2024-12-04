package com.onlineshop.product_service.service;

import com.onlineshop.product_service.dto.ProductRequest;
import com.onlineshop.product_service.dto.ProductResponse;
import com.onlineshop.product_service.model.Product;
import com.onlineshop.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.onlineshop.product_service.exception.ProductNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    public ProductResponse createProduct(ProductRequest productRequest, byte[] imageData) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .stock(productRequest.stock())
                .category(productRequest.category())
                .imageData(imageData)
                .build();

        productRepository.save(product);
        log.info("Saving product: {}", product);
        return mapToResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Retrieving all products: {}", products);
        return products.stream().map(this::mapToResponse).toList();
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return mapToResponse(product);
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest, byte[] imageData) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(productRequest.name());
        existingProduct.setDescription(productRequest.description());
        existingProduct.setPrice(productRequest.price());
        existingProduct.setStock(productRequest.stock());
        existingProduct.setCategory(productRequest.category());

        if (imageData != null) {
            existingProduct.setImageData(imageData);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Updated product: {}", updatedProduct);
        return mapToResponse(updatedProduct);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .imageData(product.getImageData())
                .build();
    }

    public List<String> getAllCategories() {
        List<String> categories = productRepository.findDistinctCategories();
        if (categories.isEmpty()) {
            log.warn("No categories found");
            return Collections.emptyList();
        }
        return categories;
    }

    public List<ProductResponse> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }

        List<Product> products = productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword.trim(), keyword.trim());
        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
