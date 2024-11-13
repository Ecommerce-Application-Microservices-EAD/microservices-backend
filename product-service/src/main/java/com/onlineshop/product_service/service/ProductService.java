package com.onlineshop.product_service.service;

import com.onlineshop.product_service.dto.ProductRequest;
import com.onlineshop.product_service.dto.ProductResponse;
import com.onlineshop.product_service.model.Product;
import com.onlineshop.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import org.springframework.stereotype.Service;
import com.onlineshop.product_service.exception.ProductNotFoundException;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Saving product: {}", product);
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Retrieving all products: {}", products);
        return products.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .build())
                .toList();
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(productRequest.name());
        existingProduct.setDescription(productRequest.description());
        existingProduct.setPrice(productRequest.price());


        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Updated product: {}", updatedProduct);
        return ProductResponse.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .build();
    }


    public void deleteProduct(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
    }


}
