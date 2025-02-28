package tests.ReqresAPITests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("RESTful API Testing")  // Defines a high-level test category
@Feature("PUT Operations")  // Specifies that this test class focuses on PUT operations
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Ensures a single test instance is used per class
public class ReqresPutTests {

    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Update")  // Groups the test under "User Update"
    @DisplayName("Update user with full data using PUT")  // Provides a readable test name
    @Severity(SeverityLevel.CRITICAL)  // Marks the test with a critical severity level
    public void testUpdateUserUsingPut() {
        // Calls a reusable method to send a PUT request with full user data
        executePutTest("/users/2", "Update User",
                new JSONObject().put("name", "John Updated").put("job", "Software Engineer"), 200);
    }

    @Test
    @Story("Partial Update")  // Groups the test under "Partial Update"
    @DisplayName("Update user with partial data using PUT")  // Provides a readable test name
    @Severity(SeverityLevel.NORMAL)  // Marks the test with a normal severity level
    public void testUpdateUserWithPartialData() {
        // Calls a reusable method to send a PUT request with only job data (partial update)
        executePutTest("/users/2", "Update User with Partial Data",
                new JSONObject().put("job", "Senior Developer"), 200);
    }

    /**
     * Executes a PUT request and validates the response.
     *
     * @param endpoint      The API endpoint for the PUT request
     * @param testName      A descriptive name for the test (for logging purposes)
     * @param requestBody   The JSON request body for the PUT request
     * @param expectedStatus The expected HTTP status code
     */
    private void executePutTest(String endpoint, String testName, JSONObject requestBody, int expectedStatus) {
        // Log the start of the test in Allure reporting
        Allure.step("Start test: " + testName);
        Allure.addAttachment("Request Payload", "application/json", requestBody.toString());

        // Send a PUT request with the request body and validate the status code
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put(BASE_URL + endpoint)
                .then()
                .statusCode(expectedStatus)  // Ensure response status is as expected
                .extract().response();

        // Attach API request details to the Allure report
        Allure.addAttachment("API Request", "text/plain", "PUT " + endpoint);
        attachResponseDetails(response);  // Log response details

        // Validate that the response body contains the expected fields
        requestBody.keySet().forEach(key ->
                response.then().body(key, equalTo(requestBody.getString(key)))
        );

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
