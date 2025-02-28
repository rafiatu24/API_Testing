package tests.ReqresAPITests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("RESTful API Testing")  // High-level category for API tests
@Feature("POST Operations")  // Specifies that this class focuses on POST operations
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Ensures that the test instance is used for the entire class lifecycle
public class ReqresPostTests {

    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    @Story("User Creation")  // Groups the test under "User Creation"
    @DisplayName("Create a new user")  // Provides a readable test name
    @Severity(SeverityLevel.CRITICAL)  // Marks the test as critical
    public void testCreateUser() {
        // Calls a reusable method to send a POST request with valid user data
        executePostTest("/users", "Create User",
                new JSONObject().put("name", "Abdul Rehman").put("job", "QA Engineer"), 201);
    }

    @Test
    @Story("Error Handling")  // Groups the test under "Error Handling"
    @DisplayName("Create user with invalid data")  // Provides a readable test name
    @Severity(SeverityLevel.NORMAL)  // Marks the test as normal severity
    public void testCreateUserWithInvalidData() {
        // Calls a reusable method to send a POST request with invalid user data
        executePostTest("/users", "Create User with Invalid Data",
                new JSONObject().put("name", ""), 201); // Note: Response should ideally return a 400 for bad request
    }

    /**
     * Executes a POST request and validates the response.
     *
     * @param endpoint      The API endpoint for the POST request
     * @param testName      A descriptive name for the test (for logging purposes)
     * @param requestBody   The JSON request body for the POST request
     * @param expectedStatus The expected HTTP status code
     */
    private void executePostTest(String endpoint, String testName, JSONObject requestBody, int expectedStatus) {
        // Log the start of the test in Allure reporting
        Allure.step("Start test: " + testName);
        Allure.addAttachment("Request Payload", "application/json", requestBody.toString());

        // Send a POST request with the request body and validate the status code
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post(BASE_URL + endpoint)
                .then()
                .statusCode(expectedStatus)  // Ensure response status is as expected
                .extract().response();

        // Attach API request details to the Allure report
        Allure.addAttachment("API Request", "text/plain", "POST " + endpoint);
        attachResponseDetails(response);  // Log response details

        // Validate that the response body contains expected fields (if they exist in the request)
        if (requestBody.has("name")) {
            response.then().body("name", equalTo(requestBody.getString("name")));
        }
        if (requestBody.has("job")) {
            response.then().body("job", equalTo(requestBody.getString("job")));
        }

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
