package gorest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRest {


    @BeforeTest
    public void beforeTest() {
        RestAssured.baseURI = "https://gorest.co.in";
    }

    //Test1 Get all users
    @Test
    public void test01_getAllUsers() {
        given().
                when()
                .get("/public/v2/users")
                .then()
                .log().body()
                .statusCode(oneOf(200, 201, 204));  // statusCode 200 ya da 201 ya da 204 olmali
    }


    @Test
    public void test02_getFirstName() {
        Object names = given()
                .when()
                .get("/public/v2/users")
                .then()
                .extract().path("name[0]");
        System.out.println(names);
    }

    @Test
    public void test03_getAllNamesThenSortThenWriteToConsol() {
        List<String> list = given()
                .when()
                .get("/public/v2/users")
                .then()
                .extract().path("name");

        list.sort(String::compareTo);
        System.out.println(list);

    }

    @Test
    public void test04_getAllFemaleNamesThenSortThenWriteToConsol() {
        List<String> list = given()
                .when()
                .get("/public/v2/users")
                .then()
                .body(not(empty()))
                .statusCode(oneOf(200, 201, 204))
                .extract().path("findAll{it.gender=='female'}.name");

        Collections.sort(list);
        System.out.println(list);
        /*
        findAll{it.gender=='female'}.name
        array icinde gender =='female' olanlarin name'lerini return eder

        users.findAll{it.gender=='male'}.name
        json icerisinde users arrayinin altinda gender=='male' olanlarin namelerini return eder

        users.name -> users icindeki name'lerin olusturdugu array
        users.name[0] -> users icindeki name'lerin olusturdugu arrayin ilk elemani
         */
    }

    @Test
    public void test05_getAllNamesWithJsonPath() {
        List<String> list = given()
                .when()
                .get("/public/v2/users")
                .then()
                .body(not(empty()))
                .statusCode(oneOf(200, 201, 204))
                .extract().jsonPath().getList("name")
//                .extract().jsonPath().get("name[0]");
                ;
        Collections.sort(list);
        System.out.println(list);

    }

    //Specifications ->spec
    @Test
    public void test06_genel() {
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://gorest.co.in")
                .build();


        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(oneOf(200, 201, 204))
                .build();

        given()
                .spec(reqSpec)
                .when()
                .get("/public/v2/users")
                .then()
                .spec(resSpec)


        ;

        /*given()
                .contentType(ContentType.JSON)
                .when()
                .get("/public/v2/users")
                .then()
                .statusCode(oneOf(200, 201, 204))
                .contentType(ContentType.JSON)
        ;*/
    }

    @Test
    public void getAUserGetInAClass() {
        User user = get("https://gorest.co.in/public/v2/users/6761988")
                .then()
                .statusCode(200)
                .log().body()
                .extract().as(User.class);
        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getEmail());

    }


    /*
    Yeni bir  class açin
    createUser'i map ile create edin
    id'yi alin
    kaydedilen user'i get yapıp user class'ina map edin
    User nesnesinin attribute'larının sout iel yazdırın
     */

}
