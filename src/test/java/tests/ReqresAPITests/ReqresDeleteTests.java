package tests.ReqresAPITests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@Epic("RESTful API Testing")  // Defines a high-level category for API testing
@Feature("DELETE Operations")  // Specifies that this class contains DELETE request tests
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Ensures a single instance of the test class is used for all tests
public class ReqresDeleteTests {

    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Management")  // Groups the test under "User Management"
    @DisplayName("Delete an existing user")  // Descriptive test name
    @Severity(SeverityLevel.CRITICAL)  // Marks the test as critical
    public void testDeleteUser() {
        // Calls the delete test method for an existing user (ID 2)
        executeDeleteTest("/users/2", "Delete Existing User");
    }

    @Test
    @Story("Error Handling")  // Groups the test under "Error Handling"
    @DisplayName("Delete a non-existing user")  // Descriptive test name
    @Severity(SeverityLevel.NORMAL)  // Marks the test as normal severity
    public void testDeleteNonExistingUser() {
        // Calls the delete test method for a non-existing user (ID 999)
        executeDeleteTest("/users/999", "Delete Non-Existing User");
    }

    /**
     * Executes a DELETE request and validates the response.
     *
     * @param endpoint The API endpoint for the DELETE request
     * @param testName A descriptive name for the test (for logging purposes)
     */
    private void executeDeleteTest(String endpoint, String testName) {
        // Log the start of the test in Allure reporting
        Allure.step("Start test: " + testName);

        // Send a DELETE request and expect a 204 No Content response
        Response response = given()
                .when()
                .delete(BASE_URL + endpoint)
                .then()
                .statusCode(204)  // 204 indicates a successful deletion with no response body
                .extract().response();

        // Attach API request details to the Allure report
        Allure.addAttachment("API Request", "text/plain", "DELETE " + endpoint);
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
                        "Headers: " + response.headers());  // Logs headers only, as DELETE typically has no response body
    }
}
