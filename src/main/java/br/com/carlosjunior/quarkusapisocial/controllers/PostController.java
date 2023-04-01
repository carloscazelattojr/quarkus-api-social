package br.com.carlosjunior.quarkusapisocial.controllers;

import br.com.carlosjunior.quarkusapisocial.dtos.CreatePostDTO;
import br.com.carlosjunior.quarkusapisocial.dtos.PostResponseDTO;
import br.com.carlosjunior.quarkusapisocial.entities.Post;
import br.com.carlosjunior.quarkusapisocial.entities.User;
import br.com.carlosjunior.quarkusapisocial.repositories.FollowerRepository;
import br.com.carlosjunior.quarkusapisocial.repositories.PostRepository;
import br.com.carlosjunior.quarkusapisocial.repositories.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostController {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;


    @Inject
    public PostController(UserRepository userRepository, PostRepository postRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostDTO postDTO) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Post post = new Post();
        post.setPostText(postDTO.getText());
        post.setUser(user);
        postRepository.persist(post);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPost(@PathParam("userId") Long userId,
                             @HeaderParam("followerId") Long followerId) {

        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (followerId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You forgot the header followerId").build();
        }

        User follower = userRepository.findById(followerId);
        if (follower == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("FollowerId not found").build();
        }

        if (!followerRepository.existsFollows(follower, user)) {
            return Response.status(Response.Status.FORBIDDEN).entity("You can't see these posts").build();
        }

        PanacheQuery<Post> query = postRepository.find("user", Sort.by("dataTime", Sort.Direction.Descending), user);
        List<Post> lista = query.list();

        List<PostResponseDTO> listaResponse = lista.stream().map(PostResponseDTO::fromEntyt).collect(Collectors.toList());
        return Response.ok(listaResponse).build();
    }

}
