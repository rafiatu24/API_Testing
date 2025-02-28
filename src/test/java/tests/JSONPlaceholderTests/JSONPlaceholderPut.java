package tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API Testing") // Defines the epic category in Allure reports
@Feature("PUT Operations") // Specifies that these tests cover PUT operations
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Uses a single test instance for the class
public class JSONPlaceholderPut {

    // Base URL for JSONPlaceholder API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    /**
     * Test to update a post using a PUT request.
     * Verifies that the response status is 200 and the returned data matches the request payload.
     */
    @Test
    @Story("Post Update") // Defines the user story in Allure reports
    @DisplayName("Update a post using PUT") // Descriptive name for the test
    @Severity(SeverityLevel.CRITICAL) // Marks the severity level
    public void testUpdatePostUsingPut() {
        executePutTest("/posts/1", "Update Post",
                new JSONObject().put("title", "Updated Title").put("body", "Updated Body").put("userId", 1), 200);
    }

    /**
     * Test to update a post with partial data using a PUT request.
     * Even with partial data, a PUT request generally replaces the entire resource.
     */
    @Test
    @Story("Partial Update")
    @DisplayName("Update a post with partial data using PUT")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdatePostWithPartialData() {
        executePutTest("/posts/1", "Update Post with Partial Data",
                new JSONObject().put("title", "Partially Updated Title"), 200);
    }

    /**
     * Helper method to execute a PUT request, validate the response, and log results in Allure.
     *
     * @param endpoint       API endpoint for the PUT request.
     * @param testName       Descriptive name of the test.
     * @param requestBody    JSON object containing the request payload.
     * @param expectedStatus Expected HTTP status code.
     */
    private void executePutTest(String endpoint, String testName, JSONObject requestBody, int expectedStatus) {
        Allure.step("Start test: " + testName); // Logs test start in Allure
        Allure.addAttachment("Request Payload", "application/json", requestBody.toString()); // Attaches request data

        // Sending PUT request and validating response status
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put(BASE_URL + endpoint)
                .then()
                .statusCode(expectedStatus) // Verifies the response status
                .extract().response();

        // Attaching request details to Allure report
        Allure.addAttachment("API Request", "text/plain", "PUT " + endpoint);
        attachResponseDetails(response); // Calls helper method to log response details

        // Verifies that each field in the request body is correctly updated in the response
        requestBody.keySet().forEach(key ->
                response.then().body(key, equalTo(requestBody.get(key)))
        );

        Allure.step("Finish test: " + testName); // Logs test completion in Allure
    }

    /**
     * Test attempting to update a non-existing post.
     * The expected status is flexible as APIs may return 200, 201, 404, or even 500.
     */
    @Test
    public void testUpdateNonExistingPost() {
        given()
                .header("Content-Type", "application/json") // Sets request content type
                .body("{\"title\": \"Updated Title\"}") // Partial update payload
                .when()
                .put(BASE_URL + "/posts/9999") // Attempts to update a non-existent resource
                .then()
                .statusCode(anyOf(is(200), is(201), is(404), is(500))); // Accepts multiple possible statuses
    }

    /**
     * Helper method to attach API response details to the Allure report.
     *
     * @param response Response object from the API call.
     */
    private void attachResponseDetails(Response response) {
        Allure.addAttachment("API Response", "text/plain",
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.headers() + "\n" +
                        "Response Body: " + response.getBody().asPrettyString());
    }
}
