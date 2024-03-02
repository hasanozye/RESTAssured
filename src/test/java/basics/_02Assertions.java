package basics;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _02Assertions {

    @BeforeTest
    public void beforeTest() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    public void test01() {
        given()
                .pathParams("id", 2)
                .when()
                .get("/api/users/{id}")
                .then()
                .statusCode(200)
                .log().body()
                .body("data.first_name", equalTo("Janet")) // Adjusted to remove "data."
                .body("data.last_name", equalTo("Weaver")) // Adjusted to remove "data."
                .body("data", hasKey("id"))
                .body("support", hasKey("url"))
                .body(containsString("contributions"))
                .body("data.id", lessThanOrEqualTo(2))
        ;
    }

    @Test(dataProvider = "getIds")
    public void test02_DataProvider(int id) {
        given()
                .pathParams("id", id)
                .when()
                .get("/api/users/{id}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @DataProvider
    public Object[][] getIds() {
        return new Object[][]{
                {1},
                {2},
                {3},
        };
    }
}
