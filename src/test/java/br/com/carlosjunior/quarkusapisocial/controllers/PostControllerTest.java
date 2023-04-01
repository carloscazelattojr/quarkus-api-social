package br.com.carlosjunior.quarkusapisocial.controllers;

import br.com.carlosjunior.quarkusapisocial.dtos.CreatePostDTO;
import br.com.carlosjunior.quarkusapisocial.entities.Follower;
import br.com.carlosjunior.quarkusapisocial.entities.Post;
import br.com.carlosjunior.quarkusapisocial.entities.User;
import br.com.carlosjunior.quarkusapisocial.repositories.FollowerRepository;
import br.com.carlosjunior.quarkusapisocial.repositories.PostRepository;
import br.com.carlosjunior.quarkusapisocial.repositories.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostController.class)
class PostControllerTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;


    @BeforeEach
    @Transactional
    public void SetUp(){
        //Usuário padrão dos testes
        User user = new User();
        user.setName("Carlos");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();

        //Criado post para o Usuário.
        Post post = new Post();
        post.setPostText("Teste teste");
        post.setUser(user);
        postRepository.persist(post);


        //Usuário Não seguidor.
        User userNotFollower = new User();
        userNotFollower.setName("Joao");
        userNotFollower.setAge(51);
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //Usuário Seguidor
        User userFollower = new User();
        userFollower.setName("Mateus");
        userFollower.setAge(11);
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);
    }
    

    @Test
    @DisplayName("should create a post for a user")
    public void createTPostTest() {
        CreatePostDTO postRequest = new CreatePostDTO();
        postRequest.setText("Aqui vai o texto");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when()
                .post()
                .then().statusCode(201);
    }


    @Test
    @DisplayName("should return 404 when trying to make a post for an inexistent user")
    public void savePostForAnInexistentUserTest(){
        CreatePostDTO postRequest = new CreatePostDTO();
        postRequest.setText("Aqui vai o texto");

        Long inexistentUserId = 9999L;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistentUserId)
                .when()
                .post()
                .then().statusCode(404);
    }


    @Test
    @DisplayName("should return 404 when user doesn't exit")
    public void listPostUserNotFoundTest(){
        Long inexistentUserId = 9999L;
        given()
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(404);
    }



    @Test
    @DisplayName("should return 400 when followerId header is not present")
    public void listPostFollowerHeaderNotSendTest(){
        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));
    }


    @Test
    @DisplayName("should return 404 when follower doesn't exit")
    public void listPostFollowerNotFoundTest(){

        Long inexistentFollowerId = 9999L;

        given()
                .pathParam("userId", userId)
                .header("followerId",inexistentFollowerId)
                .when()
                .get()
                .then()
                .statusCode(404)
                .body(Matchers.is("FollowerId not found"));
    }


    @Test
    @DisplayName("should return 403 when follower isn't a follower")
    public void listPostNotAFollowerTest(){
        given()
                .pathParam("userId", userId)
                .header("followerId",userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You can't see these posts"));
    }

    @Test
    @DisplayName("should return posts")
    public void listPostTest(){
        given()
                .pathParam("userId", userId)
                .header("followerId",userFollowerId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));
    }

}