package task;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import task.pojo.User;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TaskSolutions {


    @Test
    public void task1() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .contentType(ContentType.TEXT)
                .statusCode(203)
        ;
    }

    @Test
    public void task2() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .contentType(ContentType.TEXT)
                .statusCode(203)
                .body(equalToIgnoringCase("203 Non-Authoritative Information"))
        ;
    }

    @Test
    public void task3() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;
    }

    @Test
    public void test4() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
        ;
    }

    @Test
    public void task5() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("userId[2]", equalTo(1))
                .body("title[2]", equalTo("fugiat veniam minus"));
    }

    @Test
    public void task6() {
        ToDo toDo = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .extract().as(ToDo.class);
        System.out.println(toDo);
    }

    @Test
    public void task7() {
        List<ToDo> toDoList =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getList("", ToDo.class);

        System.out.println(toDoList);
    }

    @Test
    public void task7_2() {
        ToDo[] toDoArray =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(ToDo[].class);

        System.out.println(Arrays.toString(toDoArray));
    }

    @Test
    public void Test8() {
        Response response = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .contentType(ContentType.JSON)
                .extract().response();

        List<User> users = response.jsonPath().getList("", User.class);


    }

    @Test
    public void Test9(){
        int id = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .contentType(ContentType.JSON)
                .extract().path("find{it.name=='Ervin Howell'}.id");


        List<String> list = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/albums")
                .then()
                .contentType(ContentType.JSON)
                .extract().jsonPath().getList("findAll{it.userId==" + id + "}.title");

        for (String s : list) {
            System.out.println(s);
        }
        System.out.println(list.size());


    }

}
