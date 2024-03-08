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
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.*;

public class Rest2 {


    RequestSpecification reqSpec;
    ResponseSpecification resSpec;

    @BeforeTest
    public void beforeTest() {
        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://gorest.co.in")
                .addHeader("Authorization", "Bearer ecb8cf5d97b3ac267e2add8243779043280018a78c26f40e9c5f2e2642f2c4ff")
                .build();

        resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(oneOf(200, 201, 204))
                .expectBody(not(empty()))
                .build();
    }

    int id;

    @Test
    public void test1() {

        Response response = given()
                .spec(reqSpec)
                .body(getUserMapped())
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .spec(resSpec).extract().response();

        id = response.jsonPath().get("id");
        System.out.println(id);
    }

    @Test(dependsOnMethods = "test1")
    public void getUserInClass() {
        User user = given()
                .spec(reqSpec)
                .when()
                .get("/public/v2/users/" + id)
                .then()
                .statusCode(200)
                .spec(resSpec)
                .extract().as(User.class);

        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getGender());
        System.out.println(user.getStatus());
    }

    public Map<String, String> getUserMapped() {
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

    @Test
    public void getAllUsersInClass() {
        List<User> users = given()
                .spec(reqSpec)
                .get("/public/v2/users")
                .then()
                .statusCode(200)
                .spec(resSpec)
                .log().body()
                .extract().jsonPath().getList("", User.class);

        for (User user : users) {
            System.out.println(user);
            System.out.println("-".repeat(24));
        }
    }

    @Test
    public void getResponse() {
        Response response = get("https://gorest.co.in/public/v2/users/6762681")
                .then().extract().response();

        String name = response.path("name");
        String email = response.jsonPath().getString("email");
        System.out.println("name = " + name);
        System.out.println("email = " + email);
    }

    @Test
    public void getResponse1() {
        String response = get("https://gorest.co.in/public/v2/users/6762681")
                .asString();

        String name = from(response).get("name");
        String email = from(response).get("email");
        System.out.println("name = " + name);
        System.out.println("email = " + email);
    }


}


/*
[
    {
        "id": 6762684,
        "name": "Agrata Dwivedi",
        "email": "dwivedi_agrata@fay.test",
        "gender": "female",
        "status": "inactive"
    },
    {
        "id": 6762681,
        "name": "Chatura Khatri",
        "email": "khatri_chatura@daugherty.test",
        "gender": "female",
        "status": "active"
    },
]

    body array olarak return edilmiş
    .extract().jsonPath().getList("", User.class)
    .extract().jsonPath().getList("$", User.class)

    "", "$"  : jsonBody anlamina gelir




        json asagidaki gibi ise :
{
    "type" : "user",
    "users":[
        {
        "id": 6762684,
        "name": "Agrata Dwivedi",
        "email": "dwivedi_agrata@fay.test",
        "gender": "female",
        "status": "inactive"
    },
    {
        "id": 6762681,
        "name": "Chatura Khatri",
        "email": "khatri_chatura@daugherty.test",
        "gender": "female",
        "status": "active"
    },
    ]
}
        body object olarak return edilmiş
        List <User> listOfUser = given()...extract().jsonPath().getList("users", User.class)
        Main main = given()...extract().as(Main.class)

        class Main{
        String type;
        ArrayList<User> users;

        }

 */



