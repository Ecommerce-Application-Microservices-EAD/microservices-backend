package com.onlineshop.product_service.service;

import com.onlineshop.product_service.dto.ProductRequest;
import com.onlineshop.product_service.dto.ProductResponse;
import com.onlineshop.product_service.exception.ProductNotFoundException;
import com.onlineshop.product_service.model.Product;
import com.onlineshop.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProduct() {
        ProductRequest productRequest = new ProductRequest("iphone 16", "new iphone", new BigDecimal("999"));
        Product savedProduct = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"));

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.createProduct(productRequest);

        assertNotNull(response);
        assertEquals("iphone 16", response.name());
        assertEquals("new iphone", response.description());
        assertEquals(BigDecimal.valueOf(999), response.price());
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @Test
    void shouldGetAllProducts() {
        Product product1 = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"));
        Product product2 = new Product("2", "galaxy s20", "new galaxy", new BigDecimal("899"));

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductResponse> response = productService.getAllProducts();

        assertEquals(2, response.size());
        assertEquals("iphone 16", response.get(0).name());
        assertEquals("galaxy s20", response.get(1).name());
    }

    @Test
    void shouldGetProductById() {
        Product product = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"));
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById("1");

        assertNotNull(response);
        assertEquals("iphone 16", response.name());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById("1"));
    }

    @Test
    void shouldUpdateProduct() {
        Product existingProduct = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"));
        ProductRequest updateRequest = new ProductRequest("iphone 16", "updated iphone", new BigDecimal("1099"));
        Product updatedProduct = new Product("1", "iphone 16", "updated iphone", new BigDecimal("1099"));

        when(productRepository.findById("1")).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse response = productService.updateProduct("1", updateRequest);

        assertNotNull(response);
        assertEquals("updated iphone", response.description());
        assertEquals(BigDecimal.valueOf(1099), response.price());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentProduct() {
        ProductRequest updateRequest = new ProductRequest("iphone 16", "updated iphone", new BigDecimal("1099"));

        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct("1", updateRequest));
    }

    @Test
    void shouldDeleteProductById() {

        Product product = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"));

        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);


        productService.deleteProduct("1");


        verify(productRepository, times(1)).delete(product);
    }
    @Test
    void shouldThrowExceptionWhenDeletingNonexistentProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct("1"));
    }
}
