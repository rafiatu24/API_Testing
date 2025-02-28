package tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class for JSONPlaceholder API POST operations.
 * This class verifies the creation of posts using REST API requests.
 */
@Epic("JSONPlaceholder API Testing") // High-level category for Allure reports
@Feature("POST Operations") // Defines that the tests focus on POST operations
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Uses a single test instance for the entire class
public class JSONPlaceholderPost {

    // Base URL for JSONPlaceholder API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    /**
     * Test to create a new post using a POST request.
     * The response should have a 201 status code, indicating successful resource creation.
     */
    @Test
    @Story("Post Creation") // Defines the user story in Allure
    @DisplayName("Create a new post") // Provides a readable test name
    @Severity(SeverityLevel.CRITICAL) // Marks the test as critical
    public void testCreatePost() {
        executePostTest("/posts", "Create Post",
                new JSONObject()
                        .put("title", "Test Post")
                        .put("body", "This is a test post.")
                        .put("userId", 1), 201);
    }

    /**
     * Test to create a post with missing fields.
     * Even though some fields are missing, JSONPlaceholder's API may still return a 201 status code.
     */
    @Test
    @Story("Error Handling") // Represents handling incomplete or incorrect data
    @DisplayName("Create a post with missing fields")
    @Severity(SeverityLevel.NORMAL) // Marks the test as a lower severity level
    public void testCreatePostWithMissingFields() {
        executePostTest("/posts", "Create Post with Missing Fields",
                new JSONObject().put("title", ""), 201);
    }

    /**
     * Helper method to execute a POST request, validate the response, and attach results to Allure.
     *
     * @param endpoint       API endpoint for the POST request.
     * @param testName       Descriptive name of the test.
     * @param requestBody    JSON object containing the request payload.
     * @param expectedStatus Expected HTTP status code.
     */
    private void executePostTest(String endpoint, String testName, JSONObject requestBody, int expectedStatus) {
        Allure.step("Start test: " + testName); // Logs test start in Allure
        Allure.addAttachment("Request Payload", "application/json", requestBody.toString()); // Attaches request payload

        // Sends a POST request and validates the response
        Response response = given()
                .contentType(ContentType.JSON) // Sets the request content type
                .body(requestBody.toString()) // Attaches the request body
                .when()
                .post(BASE_URL + endpoint) // Makes the POST request
                .then()
                .statusCode(expectedStatus) // Validates the expected status code
                .extract().response(); // Extracts the response for further validation

        // Attaches request details to Allure report
        Allure.addAttachment("API Request", "text/plain", "POST " + endpoint);
        attachResponseDetails(response); // Calls method to log response details

        // Verifies that each key in the request body exists and matches in the response
        requestBody.keySet().forEach(key ->
                response.then().body(key, equalTo(requestBody.get(key)))
        );

        Allure.step("Finish test: " + testName); // Logs test completion in Allure
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
