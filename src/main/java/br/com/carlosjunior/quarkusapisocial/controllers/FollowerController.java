package br.com.carlosjunior.quarkusapisocial.controllers;

import br.com.carlosjunior.quarkusapisocial.dtos.FollowerRequestDTO;
import br.com.carlosjunior.quarkusapisocial.dtos.FollowerResponse;
import br.com.carlosjunior.quarkusapisocial.dtos.FollowersPerUserResponse;
import br.com.carlosjunior.quarkusapisocial.entities.Follower;
import br.com.carlosjunior.quarkusapisocial.entities.User;
import br.com.carlosjunior.quarkusapisocial.repositories.FollowerRepository;
import br.com.carlosjunior.quarkusapisocial.repositories.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerController {

    private FollowerRepository followerRepository;
    private UserRepository userRepository;

    @Inject
    public FollowerController(UserRepository userRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequestDTO requestDTO) {

        if (userId.equals(requestDTO.getFollowerId())) {
            return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself").build();
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(400).build();
        }

        User follower = userRepository.findById(requestDTO.getFollowerId());

        if (!followerRepository.existsFollows(follower, user)) {
            Follower entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);
            followerRepository.persist(entity);
        }

        return Response.noContent().build();
    }


    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(400).build();
        }
        List<Follower> list = followerRepository.findByUser(userId);
        FollowersPerUserResponse response = new FollowersPerUserResponse();
        response.setFollowersCount(list.size());
        List<FollowerResponse> followersList = list.stream().map(FollowerResponse::new).collect(Collectors.toList());
        response.setContent(followersList);
        return Response.ok(response).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId){
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(400).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);

        return Response.noContent().build();
    }


}
