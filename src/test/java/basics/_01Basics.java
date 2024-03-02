package basics;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

    /*
    rest-assured BDD mantigina göre yazılım vardir
    given()   -> on şartlar
    when()    -> işlemler
    then()    -> assertions

    RestAssured methodların statik importları gerekli


     */

public class _01Basics {
    @Test
    public void text01_BasicUsage() {
        given()             // ön veriler, requirementler, headers, cookies, body,.....
                .when()     // yapilan islem, GET, POST, DELETE, PUT
                .then();    // Assertions, statusCode, Json path assertions

        when()
                .get()
                .then()
        ;

    }

    @Test
    public void test02_get() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")  //GET methodu ile bu adrese request gönderdik
                .then()
//                .log().body()                                //Body verileri consola yazdırılır
//                .log().all()
//                .log().cookies()
                .log().headers()
        ;
    }

    @Test
    public void test03_statusCode() {
        String url = "https://reqres.in/api/users?page=2";
        given()
                .get(url)
                .then()
                .statusCode(201)//statusCode 200 olmali
        ;
    }

    @Test
    public void test04_ResponseTime() {
        String url = "https://reqres.in/api/users?page=2";
        long time = given()
                .get(url)
                .timeIn(TimeUnit.MILLISECONDS);

        System.out.println(time);
    }

    @Test
    public void test05_PathParams() {
        given()
                .pathParams("page", 2)
                .get("https://reqres.in/api/users?page={page}")
                .then()
                .log().body()
                .statusCode(200)
        ;

    }

    @Test
    public void test06_pathParms() {
        given()
                .pathParams("page", 1)
                .pathParams("link", "api")
                .get("https://reqres.in/{link}/users?page={page}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void test07_BaseUri() {
        RestAssured.baseURI = "https://reqres.in"; // basURL tanimi içindir

        given()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .statusCode(200)    // statusCode 200 olmali
        ;

        /*
        baseURI tanimli ise
        GET, POST, ... 'da HTTP ya da HTTPS yoksa baseURO kullanili
         */

        given()
                .get("/api/users?page=1")
                .then()
                .statusCode(200)        //statusCode 200 olmali
        ;

    }


}
