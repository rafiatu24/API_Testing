package tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class for JSONPlaceholder API GET operations.
 * This class contains tests to retrieve posts and handle different scenarios.
 */
@Epic("JSONPlaceholder API Testing") // High-level categorization for Allure reports
@Feature("GET Operations") // Defines that this class focuses on GET requests
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Uses a single test instance for the entire class
public class JSONPlaceholderGet {

    // Base URL for JSONPlaceholder API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    /**
     * Test to retrieve all posts.
     * The expected response status is 200 (OK).
     */
    @Test
    @Story("Post Listing") // Represents the scenario of retrieving multiple posts
    @DisplayName("Get all posts") // Descriptive test name
    @Severity(SeverityLevel.CRITICAL) // Marks the test as critical
    public void testGetPosts() {
        executeGetTest("/posts", "Get All Posts");
    }

    /**
     * Test to retrieve a single post by its ID.
     * The expected response status is 200 (OK).
     */
    @Test
    @Story("Post Details") // Represents the scenario of retrieving a specific post
    @DisplayName("Get a single post") // Descriptive test name
    @Severity(SeverityLevel.NORMAL) // Marks the test as normal severity
    public void testGetSinglePost() {
        executeGetTest("/posts/1", "Get Single Post");
    }

    /**
     * Test to retrieve a post that does not exist.
     * The expected response status is 404 (Not Found).
     */
    @Test
    @Story("Error Handling") // Represents handling error scenarios
    @DisplayName("Get a non-existing post") // Descriptive test name
    @Severity(SeverityLevel.MINOR) // Marks the test as minor severity
    public void testPostNotFound() {
        executeGetTest("/posts/9999", "Get Non-Existing Post", 404);
    }

    /**
     * Helper method to execute a GET request and validate the response.
     * Defaults to expecting a 200 status code.
     *
     * @param endpoint API endpoint for the GET request.
     * @param testName Descriptive name of the test.
     */
    private void executeGetTest(String endpoint, String testName) {
        executeGetTest(endpoint, testName, 200);
    }

    /**
     * Overloaded helper method to execute a GET request, validate the response, and attach results to Allure.
     *
     * @param endpoint       API endpoint for the GET request.
     * @param testName       Descriptive name of the test.
     * @param expectedStatus Expected HTTP status code.
     */
    private void executeGetTest(String endpoint, String testName, int expectedStatus) {
        Allure.step("Start test: " + testName); // Logs test start in Allure

        // Sends a GET request and validates the response status
        Response response = given()
                .when()
                .get(BASE_URL + endpoint) // Makes the GET request
                .then()
                .statusCode(expectedStatus) // Validates the expected status code
                .extract().response(); // Extracts the response for further validation

        // Attaches request details to Allure report
        Allure.addAttachment("API Request", "text/plain", "GET " + endpoint);
        attachResponseDetails(response); // Calls method to log response details

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
