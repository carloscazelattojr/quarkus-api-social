package br.com.carlosjunior.quarkusapisocial.controllers;

import br.com.carlosjunior.quarkusapisocial.dtos.FollowerRequestDTO;
import br.com.carlosjunior.quarkusapisocial.entities.Follower;
import br.com.carlosjunior.quarkusapisocial.entities.User;
import br.com.carlosjunior.quarkusapisocial.repositories.FollowerRepository;
import br.com.carlosjunior.quarkusapisocial.repositories.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
@TestHTTPEndpoint(FollowerController.class)
class FollowerControllerTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    public void SetUp() {
        //Usuário padrão dos testes
        User user = new User();
        user.setName("Carlos");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();

        //Usuário Seguidor
        User followerUser = new User();
        followerUser.setName("Maria");
        followerUser.setAge(27);
        userRepository.persist(followerUser);
        followerId = followerUser.getId();

        //criar um follower
        Follower follower = new Follower();
        follower.setFollower(followerUser);
        follower.setUser(user);
        followerRepository.persist(follower);


    }

    @Test
    @DisplayName("should return 409 when followerId is equal to User od")
    public void sameUserAsFollowerTest() {

        FollowerRequestDTO body = new FollowerRequestDTO();
        body.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then().statusCode(409).body(Matchers.is("You can't follow yourself"));

    }

    @Test
    @DisplayName("should return 404 on follow when User id doesn't exist")
    public void userNotFoundWhenTryingToFollowTest() {
        FollowerRequestDTO body = new FollowerRequestDTO();
        body.setFollowerId(userId);

        Long inexistentUserId = 9999L;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", inexistentUserId)
                .when()
                .put()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("should follow a user")
    public void followUserTest() {
        FollowerRequestDTO body = new FollowerRequestDTO();
        body.setFollowerId(followerId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then().statusCode(204);
    }


    @Test
    @DisplayName("should return 404 on list user followes  and User id doesn't exist")
    public void userNotFoundWhenListingFollowersTest() {
        Long inexistentUserId = 9999L;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then().statusCode(404);
    }


    @Test
    @DisplayName("should return list a user's followers")
    public void listFollowersTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when()
                .get()
                .then().extract().response();

        assertEquals(200,response.statusCode());

        Object followersCount = response.jsonPath().get("followersCount");
        assertEquals(1, followersCount);

        Object followersContent = response.jsonPath().getList("content");
        assertEquals(1, ((List<?>) followersContent).size());
    }


    @Test
    @DisplayName("should return 404 on unfollow user and userId doesn't exist")
    public void userNotFoundWhenUnfollowingAUserTest() {
        Long inexistentUserId = 9999L;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
                .queryParam("followId", followerId)
                .when()
                .delete()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("should return 404 on unfollow an User")
    public void unfollowUserTest() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .queryParam("followId", followerId)
                .when()
                .delete()
                .then().statusCode(204);
    }



}