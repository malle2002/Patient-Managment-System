import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:20000";
    }

    @Test
    public void shouldReturnOkWithValidToken() {
        String loginPayload = """
            {
                "email": "testuser@test.com",
                "password": "password123"
            }
        """;

        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

        System.out.println("Generated token: " + response.jsonPath().getString("token"));
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        String loginPayload = """
            {
                "email": "testuser@test.com",
                "password": "wrongpassword"
            }
        """;

        given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401);

    }
}
