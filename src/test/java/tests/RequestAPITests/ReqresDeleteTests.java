package tests.RequestAPITests;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class ReqresDeleteTests {

    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    /**
     * Test to verify that deleting an existing user returns a 204 No Content response.
     */
    @Test
    public void testDeleteUser() {
        // Start Allure step to log the test process
        Allure.step("Start test: Delete User API");

        // Step to send DELETE request and validate response
        Allure.step("Send DELETE request to remove user with ID 2", () -> {
            Response response =
                    given()
                            .when()
                            .delete(BASE_URL + "/users/2") // Sending DELETE request for user with ID 2
                            .then()
                            .statusCode(204)  // Expecting HTTP 204 No Content
                            .extract().response(); // Extract response for further validation

            // Log the response details for reporting purposes
            logResponseDetails(response);

            // Assert that the status code is indeed 204
            Assertions.assertEquals(204, response.statusCode(), "Status code should be 204");
        });

        // Finish the test case
        Allure.step("Finish testing user deletion");
    }

    /**
     * Helper method to log API response details in Allure reports.
     * @param response The API response object
     * @return String containing status code and response body
     */
    @Attachment(value = "API Response", type = "text/plain")
    private static String logResponseDetails(Response response) {
        return "Status Code: " + response.statusCode() + "\n"
                + "Response Body: " + response.getBody().asString();
    }

    /**
     * Test to verify behavior when attempting to delete a non-existing user.
     * Note: Reqres API still returns 204 for non-existent users.
     */
    @Test
    public void testDeleteNonExistingUser() {
        // Sending DELETE request for a non-existing user (ID 999)
        Response response =
                given()
                        .when()
                        .delete(BASE_URL + "/users/999") // Trying to delete user with ID 999
                        .then()
                        .statusCode(204)  // Expecting 204, as Reqres API still returns this status
                        .extract().response();

        // Assert that the API response status code is 204
        Assertions.assertEquals(204, response.statusCode(), "Status code should be 204");
    }
}
