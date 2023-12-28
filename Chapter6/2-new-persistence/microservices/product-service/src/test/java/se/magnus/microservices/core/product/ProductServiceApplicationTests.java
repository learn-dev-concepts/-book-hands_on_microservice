package se.magnus.microservices.core.product;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
public class ProductServiceApplicationTests {
	@Autowired
	private WebTestClient client;

	@Test
	public void contextLoads() {
	}

	@Test
	public void getProductById() {

		int productId = 1;

		client.get()
						.uri("/product/" + productId)
						.accept(APPLICATION_JSON)
						.exchange()
						.expectStatus().isOk()
						.expectHeader().contentType(APPLICATION_JSON)
						.expectBody()
						.jsonPath("$.productId").isEqualTo(productId);
	}

	@Test
	public void getProductInvalidParameterString() {

		client.get()
						.uri("/product/no-integer")
						.accept(APPLICATION_JSON)
						.exchange()
						.expectStatus().isEqualTo(BAD_REQUEST)
						.expectHeader().contentType(APPLICATION_JSON)
						.expectBody()
						.jsonPath("$.path").isEqualTo("/product/no-integer")
						.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getProductInvalidParameterNegativeValue() {

		int productIdInvalid = -1;

		client.get()
						.uri("/product/" + productIdInvalid)
						.accept(APPLICATION_JSON)
						.exchange()
						.expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
						.expectHeader().contentType(APPLICATION_JSON)
						.expectBody()
						.jsonPath("$.path").isEqualTo("/product/" + productIdInvalid)
						.jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
	}

}
