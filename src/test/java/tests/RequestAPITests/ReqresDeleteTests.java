package tests.RequestAPITests;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class ReqresDelete {

    private static final String BASE_URL = "https://reqres.in/api";

    @Test
    public void testDeleteUser() {
        Allure.step("Start test: Delete User API");

        Allure.step("Send DELETE request to remove user with ID 2", () -> {
            Response response =
                    given()
                            .when()
                            .delete(BASE_URL + "/users/2")
                            .then()
                            .statusCode(204)  // Expected 204 No Content for DELETE
                            .extract().response();

            logResponseDetails(response);
            Assertions.assertEquals(204, response.statusCode(), "Status code should be 204");
        });

        Allure.step("Finish testing user deletion");
    }

    @Attachment(value = "API Response", type = "text/plain")
    private static String logResponseDetails(Response response) {
        return "Status Code: " + response.statusCode() + "\n"
                + "Response Body: " + response.getBody().asString();
    }


    @Test
    public void testDeleteNonExistingUser() {
        Response response =
                given()
                        .when()
                        .delete(BASE_URL + "/users/999")
                        .then()
                        .statusCode(204)  // Reqres still returns 204 even for non-existent users
                        .extract().response();

        Assertions.assertEquals(204, response.statusCode(), "Status code should be 204");
    }
}

