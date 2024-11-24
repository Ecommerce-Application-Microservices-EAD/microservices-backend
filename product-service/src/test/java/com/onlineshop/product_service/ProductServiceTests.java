package com.onlineshop.product_service;

import com.onlineshop.product_service.dto.ProductRequest;
import com.onlineshop.product_service.dto.ProductResponse;
import com.onlineshop.product_service.exception.ProductNotFoundException;
import com.onlineshop.product_service.model.Product;
import com.onlineshop.product_service.repository.ProductRepository;
import com.onlineshop.product_service.service.ProductService;
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
        ProductRequest productRequest = new ProductRequest("iphone 16", "new iphone", new BigDecimal("999"),
                "Electronics");
        Product savedProduct = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"), "Electronics");

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
        Product product1 = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"), "Electronics");
        Product product2 = new Product("2", "galaxy s20", "new galaxy", new BigDecimal("899"), "Electronics");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductResponse> response = productService.getAllProducts();

        assertEquals(2, response.size());
        assertEquals("iphone 16", response.get(0).name());
        assertEquals("galaxy s20", response.get(1).name());
    }

    @Test
    void shouldGetProductById() {
        Product product = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"), "Electronics");
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
        Product existingProduct = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"), "Electronics");
        ProductRequest updateRequest = new ProductRequest("iphone 16", "updated iphone", new BigDecimal("1099"),
                "Electronics");
        Product updatedProduct = new Product("1", "iphone 16", "updated iphone", new BigDecimal("1099"), "Electronics");

        when(productRepository.findById("1")).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse response = productService.updateProduct("1", updateRequest);

        assertNotNull(response);
        assertEquals("updated iphone", response.description());
        assertEquals(BigDecimal.valueOf(1099), response.price());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentProduct() {
        ProductRequest updateRequest = new ProductRequest("iphone 16", "updated iphone", new BigDecimal("1099"),
                "Electronics");

        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct("1", updateRequest));
    }

    @Test
    void shouldDeleteProductById() {
        Product product = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"), "Electronics");

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

    @Test
    void shouldCreateProductWithCategory() {
        ProductRequest productRequest = new ProductRequest("iphone 16", "new iphone", new BigDecimal("999"),
                "Electronics");
        Product savedProduct = new Product("1", "iphone 16", "new iphone", new BigDecimal("999"), "Electronics");

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.createProduct(productRequest);

        assertNotNull(response);
        assertEquals("iphone 16", response.name());
        assertEquals("Electronics", response.category());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldGetAllCategories() {
        when(productRepository.findDistinctCategories()).thenReturn(Arrays.asList("Electronics", "Clothing"));

        List<String> categories = productService.getAllCategories();

        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertTrue(categories.contains("Electronics"));
        assertTrue(categories.contains("Clothing"));
        verify(productRepository, times(1)).findDistinctCategories();
    }

    @Test
    void shouldReturnEmptyCategoriesListWhenNoneExist() {
        when(productRepository.findDistinctCategories()).thenReturn(Arrays.asList());

        List<String> categories = productService.getAllCategories();

        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    void shouldSearchProductsByKeyword() {
        String keyword = "iphone";
        Product product1 = new Product("1", "iPhone 16", "Latest iPhone model", new BigDecimal("999"), "Electronics");
        Product product2 = new Product("2", "iPhone Case", "Protective case for iPhone", new BigDecimal("49"),
                "Accessories");

        when(productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword))
                .thenReturn(Arrays.asList(product1, product2));

        List<ProductResponse> response = productService.searchProducts(keyword);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("iPhone 16", response.get(0).name());
        assertEquals("iPhone Case", response.get(1).name());
        assertEquals("Accessories", response.get(1).category());
    }

    @Test
    void shouldThrowExceptionWhenSearchKeywordIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> productService.searchProducts(" "));
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsMatchSearch() {
        String keyword = "Nonexistent";
        when(productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword))
                .thenReturn(Arrays.asList());

        List<ProductResponse> response = productService.searchProducts(keyword);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }
}
