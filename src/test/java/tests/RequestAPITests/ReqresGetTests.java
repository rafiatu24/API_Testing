package tests.RequestAPITests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresGetTests {

    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    /**
     * Test to verify that retrieving a list of users returns a successful response.
     * The API call fetches users from page 2 and validates the response status.
     */
    @Test
    public void testGetUsers() {
        // Send GET request to retrieve users from page 2
        Response response = RestAssured.get(BASE_URL + "/users?page=2");

        // Validate that the response status code is 200 (OK)
        Assertions.assertEquals(200, response.statusCode(), "Status code should be 200");

        // Ensure the response contains the expected "data" field
        Assertions.assertTrue(response.getBody().asString().contains("data"),
                "Response should contain 'data' field");
    }

    /**
     * Test to verify retrieval of a specific user by ID.
     * The response should return user details with a valid email format.
     */
    @Test
    public void testSingleUser() {
        given()
                .when()
                .get(BASE_URL + "/users/2") // Fetch details of user with ID 2
                .then()
                .statusCode(200) // Ensure response status is 200 (OK)
                .body("data.id", equalTo(2)) // Validate that the returned user ID is 2
                .body("data.email", containsString("@")); // Check if the email contains '@' symbol
    }

    /**
     * Test to verify the API response when requesting a non-existing user.
     * The API should return a 404 Not Found status.
     */
    @Test
    public void testUserNotFound() {
        given()
                .when()
                .get(BASE_URL + "/users/999") // Attempt to fetch a non-existing user
                .then()
                .statusCode(404); // Expecting HTTP 404 Not Found response
    }
}
