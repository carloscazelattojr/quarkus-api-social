package br.com.carlosjunior.quarkusapisocial.repositories;

import br.com.carlosjunior.quarkusapisocial.entities.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
