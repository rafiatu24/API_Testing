package tests.RequestAPITests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.json.JSONObject;

public class ReqresPostTests {

    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    /**
     * Test to verify user creation using a POST request.
     * The request body contains a name and job, expecting a successful response with status code 201.
     */
    @Test
    public void testCreateUser() {
        // Create JSON request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "John Doe");
        requestBody.put("job", "Software Engineer");

        // Send POST request to create a new user
        Response response =
                given()
                        .contentType(ContentType.JSON) // Set request content type to JSON
                        .body(requestBody.toString()) // Attach request body
                        .when()
                        .post(BASE_URL + "/users") // Endpoint to create user
                        .then()
                        .statusCode(201) // Ensure response status is 201 (Created)
                        .body("name", equalTo("John Doe")) // Validate response body field "name"
                        .body("job", equalTo("Software Engineer")) // Validate response body field "job"
                        .body("id", not(emptyOrNullString())) // Ensure "id" is present and not empty
                        .body("createdAt", not(emptyOrNullString())) // Ensure "createdAt" timestamp is present
                        .extract().response(); // Extract full response for further assertions

        // Validate that the response status code is 201
        Assertions.assertEquals(201, response.statusCode(), "Status code should be 201");
    }

    /**
     * Test to verify API behavior when trying to create a user with invalid data.
     * An empty "name" field is provided, and we check the response status.
     */
    @Test
    public void testCreateUserWithInvalidData() {
        // Create JSON request body with an empty name field
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", ""); // Empty name (invalid input)

        // Send POST request with invalid data
        Response response =
                given()
                        .contentType(ContentType.JSON) // Set request content type to JSON
                        .body(requestBody.toString()) // Attach request body
                        .when()
                        .post(BASE_URL + "/users") // Endpoint to create user
                        .then()
                        .extract().response(); // Extract response for analysis

        // Print response details for debugging purposes
        System.out.println("Response: " + response.asString());
        System.out.println("Status Code: " + response.statusCode());

        // Validate that the response does not return a server error (500)
        Assertions.assertNotEquals(500, response.statusCode(), "Unexpected server error");

        // The expected response should be either 201 (if Reqres accepts empty fields) or 400 (Bad Request)
        Assertions.assertTrue(response.statusCode() == 201 || response.statusCode() == 400,
                "Expected 400 for invalid data, but received: " + response.statusCode());
    }
}
