package tests.RequestAPITests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Feature("ReqRes API Testing")
public class ReqresPutTests {

    private final String BASE_URL = "https://reqres.in/api";

    @Test
    @DisplayName("Update a user using PUT request")
    @Story("Update operations")
    @Description("Test PUT request to update an existing user on reqres.in")
    public void testUpdateUserUsingPut() {
        // Create JSON payload for updating a user
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "John Updated");
        requestBody.put("job", "Software Engineer");

        // Send PUT request and validate response
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put(BASE_URL + "/users/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", equalTo("John Updated"))
                .body("job", equalTo("Software Engineer"))
                // Verify that the response contains an updated timestamp
                .body("updatedAt", notNullValue())
                .extract().response();

        // Print updated timestamp
        String updatedAt = response.jsonPath().getString("updatedAt");
        System.out.println("User updated at: " + updatedAt);
    }

    @Test
    @DisplayName("Update partial user data using PATCH")
    @Story("Update operations")
    @Description("Test PATCH request to partially update a user")
    public void testPartialUpdateUsingPatch() {
        // Create JSON payload with only the fields to update
        JSONObject requestBody = new JSONObject();
        requestBody.put("job", "Senior Developer");

        // Send PATCH request and validate response
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .patch(BASE_URL + "/users/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("job", equalTo("Senior Developer"))
                .body("updatedAt", notNullValue())
                .extract().response();

        // Log response details
        System.out.println("Patch Response: " + response.asString());
    }
}