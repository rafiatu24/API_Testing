package tests.ReqresAPITests;

import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

@Epic("RESTful API Testing")  // Defines a high-level epic for test categorization
@Feature("JSON Schema Validation")  // Specifies that this test class focuses on schema validation
public class RJsonSchemaValidationTest {

    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    // Path to the folder where JSON schema files are stored
    private static final String SCHEMA_PATH = "src/test/resources/schemas/";

    @Test
    @Story("Schema Validation")  // Groups the test under the "Schema Validation" category
    @DisplayName("Validate Single User JSON Schema")  // Provides a readable name for the test
    @Severity(SeverityLevel.CRITICAL)  // Marks the test with a severity level
    public void testSingleUserJsonSchema() {
        // Calls the reusable method to validate the schema for a single user
        executeSchemaValidationTest("/users/2", "Rsingle-user-schema.json", "Validate Single User Schema");
    }

    @Test
    @Story("Schema Validation")
    @DisplayName("Validate User List JSON Schema")
    @Severity(SeverityLevel.NORMAL)
    public void testUserListJsonSchema() {
        // Calls the reusable method to validate the schema for a list of users
        executeSchemaValidationTest("/users?page=2", "RUser-list-schema.json", "Validate User List Schema");
    }

    /**
     * Executes a schema validation test for a given API endpoint.
     *
     * @param endpoint      The API endpoint to test
     * @param schemaFileName The name of the JSON schema file
     * @param testName      A descriptive name for the test (for logging purposes)
     */
    private void executeSchemaValidationTest(String endpoint, String schemaFileName, String testName) {
        // Log the start of the test in Allure reporting
        Allure.step("Start test: " + testName);

        // Send a GET request to the specified endpoint and extract the response
        Response response = given()
                .when()
                .get(BASE_URL + endpoint)
                .then()
                .statusCode(200)  // Ensure the response status is 200 OK
                .extract().response();

        // Attach API request details to the Allure report
        Allure.addAttachment("API Request", "text/plain", "GET " + endpoint);
        attachResponseDetails(response);  // Log response details

        // Load the expected JSON schema file
        File schemaFile = new File(SCHEMA_PATH + schemaFileName);

        // Attach schema file path to the Allure report
        Allure.addAttachment("JSON Schema", "text/plain", schemaFile.getAbsolutePath());

        // Validate that the response body matches the expected JSON schema
        response.then().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));

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
