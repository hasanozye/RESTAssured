package zippo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Zippo2 {


    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
        RestAssured.baseURI = "https://api.zippopotam.us";

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api.zippopotam.us")
//                .setBasePath("/TR/")
                .log(LogDetail.URI)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }

    /*
            1.  https://api.zippopotam.us/TR/06080 datasini get edin
            2. Places'in 3. elemaninin Place Name'inin Sokullu Mah. match edin.
            3. post code'un empty olmadıgını
            4. country nin  Turkey oldugunu
            5. Places'in 3. elemaninin Place Name'inin Sokullu Mah. oldugunu
            6. Places'in size'inin 18 oldugunu

     */
    @Test
    public void test1() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .body("places[2].'place name'", equalTo("Sokullu Mah."))
                .body("country", equalTo("Turkey"))
                .body("'post code'", not(equalTo(null)))
                .body("places", hasSize(18))


        ;
    }

    // places'larda tüm state'lerin ankara oldugunu assert edin
    @Test
    public void test3_GetDataAllStatesAreAnkara() {
        for (int i = 0; i < 18; i++) {


            given()
                    .spec(requestSpecification)
                    .when()
                    .get("/TR/06080")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(200)
                    .body("places[" + i + "].state", equalTo("Ankara"));
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(2, 4, 6));

        Assert.assertEquals(
                list.stream().filter(n -> n % 2 == 0).toList().size(), list.size());
    }
}
