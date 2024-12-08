package com.onlineshop.product_service;

import com.onlineshop.product_service.dto.ProductRequest;
import com.onlineshop.product_service.dto.ProductResponse;
import com.onlineshop.product_service.service.ProductService;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import com.onlineshop.product_service.exception.ProductNotFoundException;

import static org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTests {

        @LocalServerPort
        private int port;

        @MockBean
        private ProductService productService;

        @BeforeEach
        void setup() {
                RestAssured.baseURI = "http://localhost";
                RestAssured.port = port;
        }

        @Test
        void shouldGetAllProducts() {

                ProductResponse product1 = new ProductResponse(
                        "1", "iphone 16", "new iphone", new BigDecimal("999"), 50, "electronics", "image data".getBytes());
                ProductResponse product2 = new ProductResponse(
                        "2", "iphone 16 Pro", "updated iphone", new BigDecimal("1099"), 40, "electronics", "updated image data".getBytes());

                Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

                given()
                        .when()
                        .get("/api/v1/products")
                        .then()
                        .statusCode(200)
                        .body("$", hasSize(2))
                        .body("[0].id", equalTo("1"))
                        .body("[0].name", equalTo("iphone 16"))
                        .body("[1].id", equalTo("2"))
                        .body("[1].name", equalTo("iphone 16 Pro"));
        }


        @Test
        void shouldGetProductById() {

                ProductResponse mockResponse = new ProductResponse(
                        "1", "iphone 16", "new iphone", new BigDecimal("999"), 50, "electronics", "image data".getBytes());

                Mockito.when(productService.getProductById("1")).thenReturn(mockResponse);

                given()
                        .pathParam("id", "1")
                        .when()
                        .get("/api/v1/products/{id}")
                        .then()
                        .statusCode(200)
                        .body("id", equalTo("1"))
                        .body("name", equalTo("iphone 16"))
                        .body("description", equalTo("new iphone"))
                        .body("price", equalTo(999))
                        .body("stock", equalTo(50))
                        .body("category", equalTo("electronics"));
        }

        @Test
        void shouldCreateProduct() {

                String productRequestJson = """
        {
            "name": "iphone 16",
            "description": "new iphone",
            "price": 999,
            "stock": 50,
            "category": "electronics",
            "imageData": null
        }
    """;

                ProductResponse mockResponse = new ProductResponse(
                        "1",
                        "iphone 16",
                        "new iphone",
                        new BigDecimal("999"),
                        50,
                        "electronics",
                        "dummy image data".getBytes()
                );

                Mockito.when(productService.createProduct(any(ProductRequest.class), any(byte[].class)))
                        .thenReturn(mockResponse);

                given()
                        .multiPart("product", productRequestJson, "application/json") // Send the ProductRequest part as JSON
                        .multiPart("image", "image.jpg", "dummy image data".getBytes()) // Send the image as a file part
                        .contentType("multipart/form-data")
                        .when()
                        .post("/api/v1/products")
                        .then()
                        .statusCode(201)
                        .body("id", equalTo("1"))
                        .body("name", equalTo("iphone 16"))
                        .body("description", equalTo("new iphone"))
                        .body("price", equalTo(999))
                        .body("stock", equalTo(50))
                        .body("category", equalTo("electronics"));
        }

        @Test
        void shouldUpdateProduct() {

                String productRequestJson = """
        {
            "name": "iphone 16 Pro",
            "description": "updated iphone",
            "price": 1099,
            "stock": 40,
            "category": "electronics",
            "imageData": null
        }
    """;


                ProductResponse mockResponse = new ProductResponse(
                        "1",
                        "iphone 16 Pro",
                        "updated iphone",
                        new BigDecimal("1099"),
                        40,
                        "electronics",
                        "updated image data".getBytes()
                );


                Mockito.when(productService.updateProduct(anyString(), any(ProductRequest.class), any(byte[].class)))
                        .thenReturn(mockResponse);


                given()
                        .pathParam("id", "1")
                        .multiPart("product", productRequestJson, "application/json") // Send the ProductRequest part as JSON
                        .multiPart("image", "updated_image.jpg", "updated image data".getBytes()) // Send the updated image
                        .contentType("multipart/form-data")
                        .when()
                        .put("/api/v1/products/{id}")
                        .then()
                        .statusCode(200)
                        .body("id", equalTo("1"))
                        .body("name", equalTo("iphone 16 Pro"))
                        .body("description", equalTo("updated iphone"))
                        .body("price", equalTo(1099))
                        .body("stock", equalTo(40))
                        .body("category", equalTo("electronics"));
        }

        @Test
        void shouldDeleteProductById() {
                Mockito.doNothing().when(productService).deleteProduct("1");

                given()
                        .contentType("application/json")
                        .when()
                        .delete("/api/v1/products/1")
                        .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());
        }

        // failure test cases
        @Test
        void shouldGetAllProductsFailure() {
                Mockito.when(productService.getAllProducts()).thenThrow(new RuntimeException("Service failure"));

                given()
                        .contentType("application/json")
                        .when()
                        .get("/api/v1/products")
                        .then()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .body("message", equalTo("Service failure"));
        }

        @Test
        void shouldGetProductByIdFailure() {
                Mockito.when(productService.getProductById("1"))
                        .thenThrow(new ProductNotFoundException("Product not found"));

                given()
                        .contentType("application/json")
                        .when()
                        .get("/api/v1/products/1")
                        .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("message", equalTo("Product not found"));
        }

        @Test
        void shouldCreateProductFailure() {
                String productRequestJson = """
    {
        "name": "iphone 16",
        "description": "new iphone",
        "price": 999,
        "stock": 50,
        "category": "electronics",
        "imageData": null
    }
    """;

                Mockito.when(productService.createProduct(any(ProductRequest.class), any(byte[].class)))
                        .thenThrow(new RuntimeException("Failed to create product"));

                given()
                        .multiPart("product", productRequestJson, "application/json")
                        .multiPart("image", "image.jpg", "dummy image data".getBytes())
                        .contentType("multipart/form-data")
                        .when()
                        .post("/api/v1/products")
                        .then()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .body("message", equalTo("Service failure"));
        }

        @Test
        void shouldGetProductByIdNotFound() {
                Mockito.when(productService.getProductById("999"))
                        .thenThrow(new ProductNotFoundException("Product not found"));

                given()
                        .contentType("application/json")
                        .when()
                        .get("/api/v1/products/999")
                        .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("message", equalTo("Product not found"));
        }

        @Test
        void shouldGetAllProductCategories() {
                List<String> categories = Arrays.asList("Electronics", "Home Appliances", "Fashion");
                Mockito.when(productService.getAllCategories()).thenReturn(categories);

                given()
                                .contentType("application/json")
                                .when()
                                .get("/api/v1/products/categories")
                                .then()
                                .statusCode(HttpStatus.OK.value())
                                .body("size()", Matchers.equalTo(3))
                                .body("[0]", Matchers.equalTo("Electronics"))
                                .body("[1]", Matchers.equalTo("Home Appliances"))
                                .body("[2]", Matchers.equalTo("Fashion"));

                Mockito.verify(productService, Mockito.times(1)).getAllCategories();

        }

        @Test
        void shouldGetAllProductCategoriesFailure() {
                
                Mockito.when(productService.getAllCategories())
                                .thenThrow(new RuntimeException("Failed to fetch categories"));

                given()
                                .contentType("application/json")
                                .when()
                                .get("/api/v1/products/categories")
                                .then()
                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .body("message", Matchers.equalTo("Service failure"));

    // You can also verify that the service method was called exactly once
    Mockito.verify(productService, Mockito.times(1)).getAllCategories();
        }

        @Test
        void shouldSearchProductsByKeyword() {
                ProductResponse product1 = new ProductResponse("1", "iphone 16", "new iphone", new BigDecimal("999"),50,
                                "Electronics",null);
                ProductResponse product2 = new ProductResponse("2", "iphone 15", "previous model",
                                new BigDecimal("899"), 50,"Electronics",null);

                List<ProductResponse> searchResults = Arrays.asList(product1, product2);
                Mockito.when(productService.searchProducts("iphone")).thenReturn(searchResults);

                given()
                                .contentType("application/json")
                                .queryParam("keyword", "iphone")
                                .when()
                                .get("/api/v1/products/search")
                                .then()
                                .statusCode(HttpStatus.OK.value())
                                .body("size()", Matchers.equalTo(2))
                                .body("[0].id", Matchers.equalTo("1"))
                                .body("[0].name", Matchers.equalTo("iphone 16"))
                                .body("[0].description", Matchers.equalTo("new iphone"))
                                .body("[0].price", Matchers.equalTo(999))
                                .body("[1].id", Matchers.equalTo("2"))
                                .body("[1].name", Matchers.equalTo("iphone 15"))
                                .body("[1].description", Matchers.equalTo("previous model"))
                                .body("[1].price", Matchers.equalTo(899));
        }

        @Test
        void shouldSearchProductsByKeywordFailure() {
                Mockito.when(productService.searchProducts("unknown"))
                                .thenThrow(new RuntimeException("Search failed"));

                given()
                                .contentType("application/json")
                                .queryParam("keyword", "unknown")
                                .when()
                                .get("/api/v1/products/search")
                                .then()
                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

}