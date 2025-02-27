package tests.JSONPlaceholderTests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.json.JSONObject;

public class JSONPlaceholderPost {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    public void testCreatePost() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Test Post");
        requestBody.put("body", "This is a test post body.");
        requestBody.put("userId", 1);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(requestBody.toString())
                        .when()
                        .post(BASE_URL + "/posts")
                        .then()
                        .statusCode(201)
                        .body("title", equalTo("Test Post"))
                        .body("body", equalTo("This is a test post body."))
                        .body("userId", equalTo(1))
                        .body("id", not(emptyOrNullString()))
                        .extract().response();

        Assertions.assertEquals(201, response.statusCode(), "Status code should be 201");
    }

    @Test
    public void testCreatePostWithMissingFields() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "");  // Missing body and userId

        given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post(BASE_URL + "/posts")
                .then()
                .statusCode(201);  // JSONPlaceholder allows partial data, so it still returns 201
    }
}