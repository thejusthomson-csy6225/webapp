package com.csye6225.controller;

import com.csye6225.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;
    @PostConstruct
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/v1/user";
    }

    @Test
    void insertUserDetails_Success() throws Exception {

        given()
                .contentType(ContentType.JSON)
                .body(createJsonInsert(new User(null, "Thejus", "Thomson", "password","thejus@gmail.com", null, null)))
                .when()
                .post()
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(201);


        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("thejus@gmail.com", "password");

        given()
                .headers(headers)
                        .when()
                .get("/self")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("username",equalTo("thejus@gmail.com"))
                .body("firstName",equalTo("Thejus"))
                .body("lastName",equalTo("Thomson"));


        given()
                .when()
                .delete("/{username}","thejus@gmail.com")
                .then()
                .statusCode(204);
    }

    @Test
    void updateUserDetails_Success() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("thejus@gmail.com", "password");

        given()
                .contentType(ContentType.JSON)
                .body(createJsonInsert(new User(null, "Thejus", "Thomson", "password","thejus@gmail.com", null, null)))
                .when()
                .post()
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(201);


        given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(createJsonInsert(new User(null, "Wilson", "Jacob", "drowssap",null, null, null)))
                .log()
                .all()
                .when()
                .put("/self")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(204);

        headers.setBasicAuth("thejus@gmail.com", "drowssap");

        given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .when()
                .get("/self")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("firstName",equalTo("Wilson"))
                .body("lastName",equalTo("Jacob"));


        given()
                .when()
                .delete("/{username}","thejus@gmail.com")
                .then()
                .statusCode(204);
    }

    private String createJsonInsert(User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }
}