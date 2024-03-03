package localJsonServer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Odev {

    @BeforeTest
    public void beforeTest() {
        RestAssured.baseURI = "http://localhost:3000";
    }

    @Test(invocationCount = 10)
    public void test1() {
        // 10 adet user kaydi yapacak
        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@mail.com";
        String password = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(50, 100);
        String data = "{\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"password\": \"" + password + "\",\n" +
                "        \"username\": \"" + username + "\"\n" +
                "    }";
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201);
    }


    @Test(priority = 1, dependsOnMethods = "test1")
    public void test2() {
        // Tüm username'ler içindeki en uzun username'i ekrana yazdıracak
        List<String> list = given()
                .when()
                .get("/users")
                .then().
                log().all()
                .statusCode(200)
                .extract().path("username");
        System.out.println("Usernames : " + list);
        String maxLength = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).length() > maxLength.length()) {
                maxLength = list.get(i);
            }

        }
        System.out.println(maxLength);
    }

}
