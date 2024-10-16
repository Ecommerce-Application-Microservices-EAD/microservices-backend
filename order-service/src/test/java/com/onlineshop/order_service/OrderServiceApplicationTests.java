package com.onlineshop.order_service;


import com.onlineshop.order_service.stub.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.PostgreSQLContainer;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("order_service")
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
	void shouldSubmitOrder() {
		String submitOrderJson = """
                {
                     "skuCode": "iphone_15",
                     "price": 1000,
                     "quantity": 1
                }
                """;
		InventoryClientStub.stubInventoryCall("iphone_15", 1);

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.statusCode(201)
				.body("orderNumber", Matchers.notNullValue())
				.body("skuCode", Matchers.equalTo("iphone_15"))
				.body("price", Matchers.equalTo(1000))
				.body("quantity", Matchers.equalTo(1));





	}

//	@Test
//	void shouldFailOrderWhenProductIsNotInStock() {
//		String submitOrderJson = """
//                {
//                     "skuCode": "iphone_15",
//                     "price": 1000,
//                     "quantity": 1000
//                }
//                """;
//
//		RestAssured.given()
//				.contentType("application/json")
//				.body(submitOrderJson)
//				.when()
//				.post("/api/order")
//				.then()
//				.statusCode(500)
//				.log()
//				.all();
//	}

}
