package localJsonServer;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LocalTestDb {

    @Test
    public void test01_GetAllPosts() {
        given()
                .when()
                .get("http://localhost:3000/posts")
                .then()
                .log().all()
                .statusCode(200)
        ;
    }

}
