package tests.JSONPlaceholderTests;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JJsonSchemaValidationTest {

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    void testSingleUserJsonSchema() {
        given()
                .when()
                .get("/users/2")
                .then()
                .assertThat()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/schemas/jsingle-user-schema.json")));
    }

    @Test
    void testUserListJsonSchema() {
        given()
                .when()
                .get("/users?page=2")
                .then()
                .assertThat()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/schemas/juser-list-schema.json")));
    }
}