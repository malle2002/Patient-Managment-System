import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PatientIntegrationTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:20000";
    }

    @Test
    public void shouldReturnPatientsWithValidToken() {
        String loginPayload = """
            {
                "email": "testuser@test.com",
                "password": "password123"
            }
        """;

        String token = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().jsonPath().get("token");

        Response response = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/api/patients")
            .then()
            .statusCode(200)
            .body("patients", notNullValue())
            .extract().response();
        List<Map<String, Object>> patients = response.jsonPath().getList("$");

        System.out.println(patients);
    }
}
