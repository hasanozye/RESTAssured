package gorest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserTest {

    RequestSpecification reqSpec;
    ResponseSpecification resSepc;

    @BeforeTest
    public void beforeTest() {
        // sabitler, specler
        reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://gorest.co.in")
                .addHeader("Authorization", "Bearer ecb8cf5d97b3ac267e2add8243779043280018a78c26f40e9c5f2e2642f2c4ff")
                .build();

        resSepc = new ResponseSpecBuilder()
                .expectBody(not(empty()))
                .expectBody(containsString("id"))
                .expectBody(containsString("name"))
                .expectBody(containsString("email"))
                .expectBody(containsString("gender"))
                .expectBody(containsString("status"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(oneOf(200, 201, 204))
                .build();
    }


    // Test1: Create a user
    int id;
    String name;
    String gender;
    String email;
    String status;

    @Test(testName = "createUser")
    public void test1_createUser() {
//        String json = getJsonData();

        Map<String, String> json = getMapData();
        // gelen json'i response içine kaydettim
        Response response = given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .spec(resSepc)
                .extract().response();

        id = response.jsonPath().get("id");
        name = response.jsonPath().get("name");
        email = response.jsonPath().get("email");
        gender = response.jsonPath().get("gender");
        status = response.jsonPath().get("status");


        System.out.println("id : " + id);
//        Response res = get("https://gorest.co.in/public/v2/users");
//        System.out.println(res.jsonPath().prettyPrint());
    }

    // Test2: Kaydedilen json datayi guncelleyin
    @Test(dependsOnMethods = "test1_createUser")
    public void test2_updateUser() {
        Response response = given()
                .spec(reqSpec)
                .body(getJsonData())
                .when()
                .put("/public/v2/users/" + id)
                .then()
                .log().body()
                .spec(resSepc)
                .extract().response();
        System.out.println(response.jsonPath().get("name").toString());

    }

    @Test(dependsOnMethods = "test1_createUser", priority = 1)
    public void test3_deleteUser() {
        given()
                .spec(reqSpec)
                .body(getJsonData())
                .when()
                .delete("/public/v2/users/" + id)
                .then()
                .log().body()
                .statusCode(oneOf(200, 201, 204));
    }


    public String getJsonData() {
        String[] genders = {"male", "female"};
        String[] statuses = {"active", "inactive"};
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@mail.com";
        String gender = genders[new Random().nextInt(genders.length)];
        String status = statuses[new Random().nextInt(statuses.length)];

        String jsonReel = "{" +
                "\"name\":\"" + name + "\", " +
                "\"email\":\"" + email + "\", " +
                "\"gender\":\"" + gender + "\", " +
                "\"status\":\"" + status + "\"" +
                "}";

        return jsonReel;
    }

    public static Map<String, String> getMapData() {
        String[] genders = {"male", "female"};
        String[] statuses = {"active", "inactive"};

        String name = RandomStringUtils.randomAlphabetic(5, 10);
        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@mail.com";
        String gender = genders[new Random().nextInt(genders.length)];
        String status = statuses[new Random().nextInt(statuses.length)];
        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("gender", gender);
        data.put("status", status);

        return data;
    }

    public static void main(String[] args) {
        System.out.println(getMapData());
    }

}
