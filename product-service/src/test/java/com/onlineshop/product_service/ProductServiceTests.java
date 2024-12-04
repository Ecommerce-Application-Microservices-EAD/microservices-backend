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
        ProductRequest productRequest = new ProductRequest("iPhone 16", "New iPhone", new BigDecimal("999"), 50, "Electronics", new byte[]{1, 2, 3});
        Product savedProduct = Product.builder()
                .id("1")
                .name("iPhone 16")
                .description("New iPhone")
                .price(new BigDecimal("999"))
                .stock(50)
                .category("Electronics")
                .imageData(new byte[]{1, 2, 3})
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.createProduct(productRequest, productRequest.imageData());

        assertNotNull(response);
        assertEquals("iPhone 16", response.name());
        assertEquals("New iPhone", response.description());
        assertEquals(new BigDecimal("999"), response.price());
        assertEquals(50, response.stock());
        assertEquals("Electronics", response.category());
        assertArrayEquals(new byte[]{1, 2, 3}, response.imageData());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldGetAllProducts() {
        Product product1 = Product.builder()
                .id("1")
                .name("iPhone 16")
                .description("New iPhone")
                .price(new BigDecimal("999"))
                .stock(50)
                .category("Electronics")
                .imageData(new byte[]{1, 2, 3})
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Galaxy S20")
                .description("New Galaxy")
                .price(new BigDecimal("899"))
                .stock(40)
                .category("Electronics")
                .imageData(new byte[]{4, 5, 6})
                .build();

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductResponse> response = productService.getAllProducts();

        assertEquals(2, response.size());
        assertEquals("iPhone 16", response.get(0).name());
        assertEquals("Galaxy S20", response.get(1).name());
    }

    @Test
    void shouldGetProductById() {
        Product product = Product.builder()
                .id("1")
                .name("iPhone 16")
                .description("New iPhone")
                .price(new BigDecimal("999"))
                .stock(50)
                .category("Electronics")
                .imageData(new byte[]{1, 2, 3})
                .build();

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById("1");

        assertNotNull(response);
        assertEquals("iPhone 16", response.name());
        assertEquals("Electronics", response.category());
        assertArrayEquals(new byte[]{1, 2, 3}, response.imageData());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById("1"));
    }

    @Test
    void shouldUpdateProduct() {
        Product existingProduct = Product.builder()
                .id("1")
                .name("iPhone 16")
                .description("New iPhone")
                .price(new BigDecimal("999"))
                .stock(50)
                .category("Electronics")
                .imageData(new byte[]{1, 2, 3})
                .build();

        ProductRequest updateRequest = new ProductRequest("iPhone 16", "Updated iPhone", new BigDecimal("1099"), 30, "Electronics", new byte[]{4, 5, 6});
        Product updatedProduct = Product.builder()
                .id("1")
                .name("iPhone 16")
                .description("Updated iPhone")
                .price(new BigDecimal("1099"))
                .stock(30)
                .category("Electronics")
                .imageData(new byte[]{4, 5, 6})
                .build();

        when(productRepository.findById("1")).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse response = productService.updateProduct("1", updateRequest, updateRequest.imageData());

        assertNotNull(response);
        assertEquals("Updated iPhone", response.description());
        assertEquals(new BigDecimal("1099"), response.price());
        assertEquals(30, response.stock());
        assertArrayEquals(new byte[]{4, 5, 6}, response.imageData());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentProduct() {
        ProductRequest updateRequest = new ProductRequest("iPhone 16", "Updated iPhone", new BigDecimal("1099"), 30, "Electronics", new byte[]{4, 5, 6});

        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct("1", updateRequest, updateRequest.imageData()));
    }

    @Test
    void shouldDeleteProductById() {
        Product product = Product.builder()
                .id("1")
                .name("iPhone 16")
                .description("New iPhone")
                .price(new BigDecimal("999"))
                .stock(50)
                .category("Electronics")
                .imageData(new byte[]{1, 2, 3})
                .build();

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
        Product product1 = new Product("1", "iPhone 16", "Latest iPhone model", new BigDecimal("999"),50, "Electronics",null);
        Product product2 = new Product("2", "iPhone Case", "Protective case for iPhone", new BigDecimal("49"),50,
                "Accessories",null);

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
