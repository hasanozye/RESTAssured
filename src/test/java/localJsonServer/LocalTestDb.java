package localJsonServer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LocalTestDb {

    @BeforeTest
    public void beforeTest() {
        RestAssured.baseURI = "http://localhost:3000";
    }

    @Test
    public void test01_GetAllUsers() {
        given()
                .when()
                .get("/users")
                .then()
                .log().all()
                .statusCode(200)


        //hasItem -> tekdeger assert
        //hasItems -> array içinde olması beklenen değerler
        ;
    }

    //extract a value
    @Test
    public void test01_GetAllUsers_ExtractData() {
        String id = given()
                .when()
                .get("/users")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("id[0]");       // extract response'un içindeki veriye ulaşmamiz içim
        System.out.println("id[0] : " + id);      // path ,stediğimiz verinin yolunu veririz

        //hasItem -> tekdeger assert
        //hasItems -> array içinde olması beklenen değerler
        ;
    }

    @Test
    public void test01_GetAllUsers_ExtractList() {
        List<Integer> list = given()
                .when()
                .get("/users")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("id");
        System.out.println("ids : " + list);

        //hasItem -> tekdeger assert
        //hasItems -> array içinde olması beklenen değerler
        ;
    }


    @Test
    public void postdata() {
        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@mail.com";
        String password = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(5, 10);
        String data = "{\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"password\": \"" + password + "\",\n" +
                "        \"username\": \"" + username + "\"\n" +
                "    }";
        String id = given()
                .contentType(ContentType.JSON)  // body, request (giden) contentType
                .body(data)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id");

        System.out.println("Kayit id : " + id);

        String jsonForUpdate = "{\n" +
                "        \"email\": \"" + RandomStringUtils.randomAlphabetic(10, 20) + "@mail.net\",\n" +
                "        \"password\": \"" + RandomStringUtils.randomAlphabetic(5, 10) + "\",\n" +
                "        \"username\": \"" + RandomStringUtils.randomAlphabetic(5, 10) + "\"\n" +
                "    }";

        given()
                .contentType(ContentType.JSON)
                .body(jsonForUpdate)
                .when()
                .put("/users/" + id)
                .then()
                .log().body()
                .statusCode(200)
        ;

        given()
                .when()
                .delete("/users/" + id)
                .then()
                .statusCode(200)
        ;

    }

    /*
    Yeni bir class'da
    Test1: post ile random 10 kayit ekleyin
    Test2: users'larin username'leri liste olarak alin
           max uzunluktaki username'i ekrana yazdirin
     */

    public static void main(String[] args) {
        System.out.println(getRandomString(20, 30));
    }

    // getRandonString(3,5) ->Ab5c
    public static String getRandomString(int min, int max) {
        String str = "abcdefABCDEF012345";
        String rndStr = "";

        int last = min + new Random().nextInt(max - min);
        for (int i = 0; i < last; i++) {
            rndStr += str.charAt(new Random().nextInt(str.length()));
        }
        return rndStr;
    }

}
