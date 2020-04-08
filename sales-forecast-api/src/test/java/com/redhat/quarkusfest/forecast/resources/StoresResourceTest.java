package com.redhat.quarkusfest.forecast.resources;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
public class StoresResourceTest {

	@Test
	public void testHelloEndpoint() {
		RestAssured.given().when().get("/api/stores").then().statusCode(200);
	}

}