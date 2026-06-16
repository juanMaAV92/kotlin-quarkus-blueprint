package com.juanmaav.blueprint.api.controllers

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test

@QuarkusTest
class UserControllerTest {
    @Test
    fun `should create user and return response with id`() {
        given()
            .contentType(ContentType.JSON)
            .body("""{"name":"Juan","email":"juan@test.com","age":25}""")
            .`when`()
            .post("/users")
            .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("name", equalTo("Juan"))
            .body("email", equalTo("juan@test.com"))
            .header("X-Trace-Id", notNullValue())
    }

    @Test
    fun `should return validation errors for invalid request`() {
        given()
            .contentType(ContentType.JSON)
            .body("""{"name":"","email":"invalid","age":15}""")
            .`when`()
            .post("/users")
            .then()
            .statusCode(500)
            .body("code", equalTo("VALIDATION_FAILED"))
            .body("messages.size()", equalTo(3))
    }

    @Test
    fun `should return 404 for unknown endpoint`() {
        given()
            .`when`()
            .get("/nonexistent")
            .then()
            .statusCode(404)
            .body("code", equalTo("NOT_FOUND"))
    }

    @Test
    fun `should get a user by id after creating it`() {
        val id =
            given()
                .contentType(ContentType.JSON)
                .body("""{"name":"Grace","email":"grace@test.com","age":40}""")
                .`when`()
                .post("/users")
                .then()
                .statusCode(200)
                .extract()
                .path<String>("id")

        given()
            .`when`()
            .get("/users/$id")
            .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Grace"))
    }

    @Test
    fun `should return 404 when getting a missing user`() {
        given()
            .`when`()
            .get("/users/does-not-exist")
            .then()
            .statusCode(404)
            .body("code", equalTo("USER_NOT_FOUND"))
    }

    @Test
    fun `should list users with pagination metadata`() {
        given()
            .contentType(ContentType.JSON)
            .body("""{"name":"Linus","email":"linus@test.com","age":35}""")
            .`when`()
            .post("/users")
            .then()
            .statusCode(200)

        given()
            .`when`()
            .get("/users?page=1&limit=10")
            .then()
            .statusCode(200)
            .body("users", notNullValue())
            .body("page", equalTo(1))
            .body("limit", equalTo(10))
            .body("total", notNullValue())
    }
}
