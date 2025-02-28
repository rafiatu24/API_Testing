package tests.ReqresAPITests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@Epic("RESTful API Testing")  // High-level category for API testing
@Feature("GET Operations")  // Specifies that this class contains GET request tests
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Ensures a single instance of the test class is used for all tests
public class ReqresGetTests {

    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Listing")  // Groups the test under "User Listing"
    @DisplayName("Get list of users")  // Provides a readable test name
    @Severity(SeverityLevel.CRITICAL)  // Marks the test as critical
    public void testGetUsers() {
        executeGetTest("/users?page=2", "Get User List");
    }

    @Test
    @Story("User Details")  // Groups the test under "User Details"
    @DisplayName("Get single user details")  // Provides a readable test name
    @Severity(SeverityLevel.NORMAL)  // Marks the test as normal severity
    public void testSingleUser() {
        executeGetTest("/users/2", "Get Single User");
    }

    @Test
    @Story("Error Handling")  // Groups the test under "Error Handling"
    @DisplayName("Get non-existing user")  // Provides a readable test name
    @Severity(SeverityLevel.MINOR)  // Marks the test as minor severity
    public void testUserNotFound() {
        // Calls a reusable method to send a GET request with an expected 404 status code
        executeGetTest("/users/999", "Get Non-Existing User", 404);
    }

    /**
     * Executes a GET request and validates the response status code (default: 200).
     *
     * @param endpoint The API endpoint for the GET request
     * @param testName A descriptive name for the test (for logging purposes)
     */
    private void executeGetTest(String endpoint, String testName) {
        executeGetTest(endpoint, testName, 200);
    }

    /**
     * Executes a GET request and validates the response.
     *
     * @param endpoint      The API endpoint for the GET request
     * @param testName      A descriptive name for the test (for logging purposes)
     * @param expectedStatus The expected HTTP status code
     */
    private void executeGetTest(String endpoint, String testName, int expectedStatus) {
        // Log the start of the test in Allure reporting
        Allure.step("Start test: " + testName);

        // Send a GET request and validate the status code
        Response response = given()
                .when()
                .get(BASE_URL + endpoint)
                .then()
                .statusCode(expectedStatus)  // Ensure response status is as expected
                .extract().response();

        // Attach API request details to the Allure report
        Allure.addAttachment("API Request", "text/plain", "GET " + endpoint);
        attachResponseDetails(response);  // Log response details

        // Log the completion of the test in Allure
        Allure.step("Finish test: " + testName);
    }

    /**
     * Attaches API response details to the Allure report for better debugging.
     *
     * @param response The API response to log
     */
    private void attachResponseDetails(Response response) {
        Allure.addAttachment("API Response", "text/plain",
                "Status Code: " + response.statusCode() + "\n" +
                        "Headers: " + response.headers() + "\n" +
                        "Response Body: " + response.getBody().asPrettyString());
    }
}
