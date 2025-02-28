package tests.JSONPlaceholderTests;

import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;

import static io.restassured.RestAssured.given;

@Epic("JSONPlaceholder API Testing") // High-level test category for Allure reporting
@Feature("JSON Schema Validation")  // Feature being tested: Schema validation
public class PJsonValidationSchemaTest {

    // Base URL for JSONPlaceholder API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    // Path to the folder containing JSON schema files
    private static final String SCHEMA_PATH = "src/test/resources/schemas/";

    @Test
    @Story("Schema Validation")  // Groups the test under "Schema Validation"
    @DisplayName("Validate Post List JSON Schema")  // Descriptive test name for better reporting
    @Severity(SeverityLevel.CRITICAL)  // Marks this test as critical
    public void testGetJsonSchema() {
        // Validates the schema for the list of posts
        executeSchemaValidationTest("/posts", "JUser-list-schema.json", "Validate Post List Schema");
    }

    @Test
    @Story("Schema Validation")  // Groups the test under "Schema Validation"
    @DisplayName("Validate Single Post JSON Schema")  // Descriptive test name
    @Severity(SeverityLevel.NORMAL)  // Marks this test as normal severity
    public void testGetAllJsonSchema() {
        // Validates the schema for a single post
        executeSchemaValidationTest("/posts/1", "Jsingle-user-schema.json", "Validate Single Post Schema");
    }

    /**
     * Executes a JSON schema validation test for a given endpoint.
     *
     * @param endpoint      The API endpoint to test
     * @param schemaFileName The name of the JSON schema file for validation
     * @param testName      A descriptive name for the test (used for logging)
     */
    private void executeSchemaValidationTest(String endpoint, String schemaFileName, String testName) {
        Allure.step("Start test: " + testName);  // Logs the test start in Allure

        // Sends a GET request and extracts the response
        Response response = given()
                .when()
                .get(BASE_URL + endpoint)
                .then()
                .statusCode(200)  // Expects a 200 OK response
                .extract().response();

        // Attach API request details to the Allure report
        Allure.addAttachment("API Request", "text/plain", "GET " + endpoint);
        attachResponseDetails(response);

        // Load the JSON schema file for validation
        File schemaFile = new File(SCHEMA_PATH + schemaFileName);
        Allure.addAttachment("JSON Schema", "text/plain", schemaFile.getAbsolutePath());

        // Validate the API response against the specified JSON schema
        response.then().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));

        Allure.step("Finish test: " + testName);  // Logs the test completion in Allure
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
