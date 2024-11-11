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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductServiceUnitTests {

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
    void shouldCreateProduct() {
        ProductRequest productRequest = new ProductRequest("iphone 16", "new iphone", new BigDecimal("999"));
        ProductResponse productResponse = new ProductResponse("1", "iphone 16", "new iphone", new BigDecimal("999"));

        Mockito.when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        given()
                .contentType("application/json")
                .body(productRequest)
                .when()
                .post("/api/product")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.equalTo("1"))
                .body("name", Matchers.equalTo("iphone 16"))
                .body("description", Matchers.equalTo("new iphone"))
                .body("price", Matchers.equalTo(999));
    }

    @Test
    void shouldCreateProductFailure() {

    }


    @Test
    void shouldGetAllProducts() {
        ProductResponse product1 = new ProductResponse("1", "iphone 16", "new iphone", new BigDecimal("999"));
        ProductResponse product2 = new ProductResponse("2", "galaxy s20", "new galaxy", new BigDecimal("999"));

        List<ProductResponse> productList = Arrays.asList(product1, product2);
        Mockito.when(productService.getAllProducts()).thenReturn(productList);

        given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(2))
                .body("[0].id", Matchers.equalTo("1"))
                .body("[0].name", Matchers.equalTo("iphone 16"))
                .body("[0].description", Matchers.equalTo("new iphone"))
                .body("[0].price", Matchers.equalTo(999))
                .body("[1].id", Matchers.equalTo("2"))
                .body("[1].name", Matchers.equalTo("galaxy s20"))
                .body("[1].description", Matchers.equalTo("new galaxy"))
                .body("[1].price", Matchers.equalTo(999));
    }


    @Test
    void shouldGetAllProductsFailure() {
        Mockito.when(productService.getAllProducts()).thenThrow(new RuntimeException("Database error"));

        given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void shouldGetProductById() {
        ProductResponse productResponse = new ProductResponse("1", "iphone 16", "new iphone", new BigDecimal("999"));
        Mockito.when(productService.getProductById("1")).thenReturn(productResponse);

        given()
                .contentType("application/json")
                .when()
                .get("/api/product/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo("1"))
                .body("name", Matchers.equalTo("iphone 16"))
                .body("description", Matchers.equalTo("new iphone"))
                .body("price", Matchers.equalTo(999));
    }

    @Test
    void shouldGetProductByIdFailure() {

    }


    @Test
    void shouldUpdateProductById() {
        ProductRequest updateRequest = new ProductRequest("iphone 16", "updated iphone", new BigDecimal("1099"));
        ProductResponse updatedResponse = new ProductResponse("1", "iphone 16", "updated iphone", new BigDecimal("1099"));

        Mockito.when(productService.updateProduct(anyString(), any(ProductRequest.class))).thenReturn(updatedResponse);

        given()
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .put("/api/product/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo("1"))
                .body("name", Matchers.equalTo("iphone 16"))
                .body("description", Matchers.equalTo("updated iphone"))
                .body("price", Matchers.equalTo(1099));
    }

    @Test
    void shouldUpdateProductByIdFailure() {
        ProductRequest updateRequest = new ProductRequest("iphone 16", "updated iphone", new BigDecimal("1099"));

        Mockito.when(productService.updateProduct(anyString(), any(ProductRequest.class)))
                .thenThrow(new RuntimeException("Update failed"));

        given()
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .put("/api/product/1")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void shouldDeleteProductById() {
        Mockito.doNothing().when(productService).deleteProduct("1");

        given()
                .contentType("application/json")
                .when()
                .delete("/api/product/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldDeleteProductByIdFailure() {
        Mockito.doThrow(new RuntimeException("Delete failed")).when(productService).deleteProduct("1");

        given()
                .contentType("application/json")
                .when()
                .delete("/api/product/1")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
