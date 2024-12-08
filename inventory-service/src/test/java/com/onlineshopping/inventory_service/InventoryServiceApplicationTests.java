package com.onlineshopping.inventory_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceTests {

	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("inventory_service")
			.withUsername("postgres")
			.withPassword("postgres");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		postgreSQLContainer.start();
	}

	@Test
	void shouldReturnTrueWhenEnoughInventoryAvailable() {
		RestAssured.given()
				.queryParam("skuCode", "iphone_15")
				.queryParam("quantity", 100)
				.when()
				.get("/api/inventory")
				.then()
				.statusCode(200)  // Check if the response status is 200 OK
				.body(equalTo("true"));  // Verify the boolean response as string
	}

	@Test
	void shouldReturnFalseWhenNotEnoughInventoryAvailable() {
		RestAssured.given()
				.queryParam("skuCode", "iphone_15")
				.queryParam("quantity", 101)
				.when()
				.get("/api/inventory")
				.then()
				.statusCode(200)  // Check if the response status is 200 OK
				.body(equalTo("false"));  // Verify the boolean response as string
	}
}
