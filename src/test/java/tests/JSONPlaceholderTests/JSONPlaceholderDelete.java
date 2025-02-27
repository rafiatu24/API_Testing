package tests.JSONPlaceholderTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class JSONPlaceholderDelete {

    // Base URL for the JSONPlaceholder API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    /**
     * Test to verify the deletion of an existing post.
     * JSONPlaceholder API returns a 200 OK response for successful DELETE operations.
     */
    @Test
    public void testDeletePost() {
        // Send DELETE request for post with ID 1
        Response response =
                given()
                        .when()
                        .delete(BASE_URL + "/posts/1") // Deleting post with ID 1
                        .then()
                        .statusCode(200)  // Expecting HTTP 200 OK as per API behavior
                        .extract().response(); // Extract response for further assertions

        // Validate that the response status code is 200
        Assertions.assertEquals(200, response.statusCode(), "Status code should be 200");
    }

    /**
     * Test to verify the behavior when attempting to delete a non-existing post.
     * JSONPlaceholder still returns a 200 OK status, even when the resource does not exist.
     */
    @Test
    public void testDeleteNonExistingPost() {
        // Send DELETE request for a non-existent post (ID 9999)
        Response response =
                given()
                        .when()
                        .delete(BASE_URL + "/posts/9999") // Trying to delete post with ID 9999
                        .then()
                        .statusCode(200)  // JSONPlaceholder still returns 200
                        .extract().response(); // Extract response for verification

        // Validate that the response status code is 200
        Assertions.assertEquals(200, response.statusCode(), "Status code should be 200");
    }
}
