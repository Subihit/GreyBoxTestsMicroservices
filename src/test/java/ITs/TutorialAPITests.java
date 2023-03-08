package ITs;

import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;

public class TutorialAPITests {

   //@Test
    public void addtutorial() {

        Header header = new Header("user", "452c4e4e-bbe0-409f-a21e-2722eda012df");

        Response response = given().
                baseUri("http://localhost:8080").
                //  and().header(header).
                        when().
                        get("api/tutorials/1").
                        then().
                        extract().response();

        Assertions.assertEquals(response.getBody().jsonPath().get("id").toString(), "1");
    }

}
