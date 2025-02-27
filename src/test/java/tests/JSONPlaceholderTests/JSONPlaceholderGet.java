package tests.JSONPlaceholderTests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

public class JSONPlaceholderGet {

    // Base URL for the JSONPlaceholder API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    /**
     * Test to verify that retrieving all posts returns a valid response.
     * The API should return a list of posts with a status code of 200.
     */
    @Test
    public void testGetPosts() {
        given()
                .when()
                .get(BASE_URL + "/posts") // Fetch all posts
                .then()
                .statusCode(200) // Ensure the response status is 200 (OK)
                .body("$.size()", greaterThan(0)); // Validate that the response contains at least one post
    }

    /**
     * Test to verify retrieving a single post by its ID.
     * The response should contain the correct post ID and a valid title.
     */
    @Test
    public void testGetSinglePost() {
        given()
                .when()
                .get(BASE_URL + "/posts/1") // Fetch post with ID 1
                .then()
                .statusCode(200) // Ensure the response status is 200 (OK)
                .body("id", equalTo(1)) // Validate that the post ID matches the requested ID
                .body("title", not(emptyOrNullString())); // Ensure the post title is not empty or null
    }

    /**
     * Test to verify API behavior when requesting a non-existent post.
     * The API should return a 404 Not Found status.
     */
    @Test
    public void testPostNotFound() {
        given()
                .when()
                .get(BASE_URL + "/posts/9999") // Attempt to fetch a non-existing post
                .then()
                .statusCode(404); // Expecting HTTP 404 Not Found response
    }
}
