package tests.JSONPlaceholderTests;

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

@Feature("JSONPlaceholder API Testing")
public class JSONPlaceholderPut{

    private final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    @DisplayName("Update a post using PUT request")
    @Story("Update operations")
    @Description("Test PUT request to update an existing post on JSONPlaceholder")
    public void testUpdatePostUsingPut() {
        // Create JSON payload for the PUT request
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 1);
        requestBody.put("title", "Updated Title");
        requestBody.put("body", "This is the updated body of the post");
        requestBody.put("userId", 1);

        // Send PUT request and validate response
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .put(BASE_URL + "/posts/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("title", equalTo("Updated Title"))
                .body("body", equalTo("This is the updated body of the post"))
                .body("userId", equalTo(1))
                .extract().response();

        // Log response details
        System.out.println("Response: " + response.asString());
    }
}