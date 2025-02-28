package tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

/**
 * Test class for JSONPlaceholder API DELETE operations.
 * This class contains tests to delete posts and handle different scenarios.
 */
@Epic("JSONPlaceholder API Testing") // High-level categorization for Allure reports
@Feature("DELETE Operations") // Defines that this class focuses on DELETE requests
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Uses a single test instance for the entire class
public class JSONPlaceholderDelete {

    // Base URL for JSONPlaceholder API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    /**
     * Test to delete an existing post.
     * The expected response status is 200 (OK), as JSONPlaceholder returns 200 even if the resource is deleted.
     */
    @Test
    @Story("Post Management") // Represents post deletion scenario
    @DisplayName("Delete an existing post") // Descriptive test name
    @Severity(SeverityLevel.CRITICAL) // Marks the test as critical
    public void testDeletePost() {
        executeDeleteTest("/posts/1", "Delete Existing Post");
    }

    /**
     * Test to delete a post that does not exist.
     * JSONPlaceholder API still returns 200 (OK) even if the post does not exist.
     */
    @Test
    @Story("Error Handling") // Represents handling error scenarios
    @DisplayName("Delete a non-existing post") // Descriptive test name
    @Severity(SeverityLevel.NORMAL) // Marks the test as normal severity
    public void testDeleteNonExistingPost() {
        executeDeleteTest("/posts/9999", "Delete Non-Existing Post");
    }

    /**
     * Helper method to execute a DELETE request and validate the response.
     *
     * @param endpoint API endpoint for the DELETE request.
     * @param testName Descriptive name of the test.
     */
    private void executeDeleteTest(String endpoint, String testName) {
        Allure.step("Start test: " + testName); // Logs test start in Allure

        // Sends a DELETE request and validates the response status
        Response response = given()
                .when()
                .delete(BASE_URL + endpoint) // Makes the DELETE request
                .then()
                .statusCode(200) // JSONPlaceholder always returns 200 for DELETE requests
                .extract().response(); // Extracts the response for further validation

        // Attaches request details to Allure report
        Allure.addAttachment("API Request", "text/plain", "DELETE " + endpoint);
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
                        "Headers: " + response.headers());
    }
}
