package br.com.carlosjunior.quarkusapisocial.controllers;

import br.com.carlosjunior.quarkusapisocial.dtos.CreateUserDTO;
import br.com.carlosjunior.quarkusapisocial.entities.User;
import br.com.carlosjunior.quarkusapisocial.exceptions.ResponseError;
import br.com.carlosjunior.quarkusapisocial.repositories.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private UserRepository repository;
    private Validator validator;

    @Inject
    public UserController(UserRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserDTO userDTO) {

        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(userDTO);
        if (!violations.isEmpty()) {
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        repository.persist(user);
        return Response.status(201).entity(user).build();
    }

    @GET
    public Response listAllUser() {
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        User user = repository.findById(id);
        if (user != null) {
            repository.delete(user);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserDTO userDTO) {
        User user = repository.findById(id);
        if (user != null) {
            user.setName(userDTO.getName());
            user.setAge(userDTO.getAge());
            repository.persist(user);
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


}
