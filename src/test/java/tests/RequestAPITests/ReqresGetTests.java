package api.tests.ReqesAPITests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresGetTests {

    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    public void testGetUsers() {
        Response response = RestAssured.get(BASE_URL + "/users?page=2");

        Assertions.assertEquals(200, response.statusCode(), "Status code should be 200");
        Assertions.assertTrue(response.getBody().asString().contains("data"), "Response should contain 'data' field");
    }

    @Test
    public void testSingleUser() {
        given()
                .when()
                .get(BASE_URL + "/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", containsString("@"));
    }

    @Test
    public void testUserNotFound() {
        given()
                .when()
                .get(BASE_URL + "/users/999")
                .then()
                .statusCode(404);
    }
}