package br.com.carlosjunior.quarkusapisocial.controllers;

import br.com.carlosjunior.quarkusapisocial.dtos.CreateUserDTO;
import br.com.carlosjunior.quarkusapisocial.exceptions.ResponseError;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @TestHTTPResource("/users")
    URL apiURL;

    @Test
    @DisplayName("should create an user successfully")
    @Order(1)
    public void createUserTest() {
        CreateUserDTO user = new CreateUserDTO();
        user.setName("Carlos");
        user.setAge(39);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiURL)
                .then().extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));

    }

    @Test
    @DisplayName("should return error when json is not valid")
    @Order(2)
    public void createUserValidationErrorTest() {
        CreateUserDTO user = new CreateUserDTO();
        user.setName(null);
        user.setAge(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiURL)
                .then().extract().response();


        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
//        assertEquals("Nome é obrigatório", errors.get(0).get("message"));
//        assertEquals("Idade é obrigatório", errors.get(1).get("message"));

    }

    @Test
    @DisplayName("should list all users")
    @Order(3)
    public void listAllUsersTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiURL)
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));

    }


}