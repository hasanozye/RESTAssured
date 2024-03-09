package zippo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import zippo.pojoclasses.Location;
import zippo.pojoclasses.Place;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
/*       for (int i = 0; i < 18; i++) {


            given()
                    .spec(requestSpecification)
                    .when()
                    .get("/TR/06080")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(200)
                    .body("places[" + i + "].state", equalTo("Ankara"));}*/


        // json'daki places'in siez'i 18 dir
        given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}", hasSize(0))

        ;
    }


    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(2, 4, 6));

        int evenNumCount = (int) list.stream().filter(n -> n % 2 == 0).count();

        int oddNumCount = (int) list.stream().filter(n -> n % 2 == 1).count();
    }


    // TR ve 06080 yerine pathParam kullaniniz
    @Test
    public void test4_getDataUsePathParam() {

        String country = "TR";
        String postCode = "06080";
        given()
                .spec(requestSpecification)
                .pathParams("ulke", country)
                .pathParams("postaKodu", postCode)
                .when()
                .get("/{ulke}/{postaKodu}")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}", hasSize(0))
        ;
    }

    // country extract edin ve Turkey oldugun assert edin
    // 3. mahallenin adini extract edin ve Sokullu Mah. oldugunu assert edin
    @Test
    public void test5_getDataExtractPlaceName() {

        /*


        String placeName = "";
        */

        String country = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().path("country");

//        String country = "";
        Assert.assertEquals(country, "Turkey");

        String placeName = given()
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().path("places[2].'place name'");
        Assert.assertEquals(placeName, "Sokullu Mah.");

    }


    @Test
    public void test5_getDataExtractPlaceName1() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

        String country = response.then().extract().path("country");
        Assert.assertEquals(country, "Turkey");

        String placeName = response.then().extract().jsonPath().get("places[2].'place name'");
        Assert.assertEquals(placeName, "Sokullu Mah.");
        response.prettyPrint();

    }


    // mahalle isimlerini liste olarak extract edin, size 18 oldugnu assert edin

    @Test
    public void test5_getDataExtractMahalleName1() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

        List<String> placeNames = response.then().extract().path("places.'place name'");
        Assert.assertEquals(placeNames.size(), 18);
        String longestPlaceName = "";
        for (String placeName : placeNames) {
            if (placeName.length() > longestPlaceName.length()) {
                longestPlaceName = placeName;
            }
        }
        System.out.println("placeNames = " + placeNames);
        System.out.println("En uzun 'place name': " + longestPlaceName);
        System.out.println("Uzunluğu: " + longestPlaceName.length());

    }


    // json datasini pojo'ya map edin
    @Test
    public void test8_getDataToPojo() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

        Location location = response.then().extract().as(Location.class);

        System.out.println(location.getPlaces());
        System.out.println(location.getPlaces().get(0).getPlaceName());

    }

    // Ankaranin tüm mahallelerini bulun

    @Test
    public void test9_getDataToPojo() throws IOException {

        FileWriter fileWriter = new FileWriter("Places.txt");
        for (int i = 6070; i < 6090; i++) {
            String postCode = getPostaKodu(i);
            Response response = given()
                    .spec(requestSpecification)
                    .pathParams("postaKodu", postCode)
                    .when()
                    .get("/TR/{postaKodu}")
                    .then()
                    .extract().response();
            Location location = response.then().extract().as(Location.class);
            if (location.getPlaces() != null) {
                for (Place place : location.getPlaces()) {
                    String str = location.getCountry() + "\t" +
                            place.getState() + "\t" +
                            place.getPlaceName() + "\n";

                    fileWriter.write(str);
                }
            }
        }
        fileWriter.close();
    }

    public String getPostaKodu(int num) {
        String code = String.valueOf(num);

        for (int i = code.length(); i < 5; i++) {
            code = "0".concat(code);
        }
        return code;
    }
}
