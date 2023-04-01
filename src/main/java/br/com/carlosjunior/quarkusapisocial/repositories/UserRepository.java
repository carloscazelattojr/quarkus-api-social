package br.com.carlosjunior.quarkusapisocial.repositories;

import br.com.carlosjunior.quarkusapisocial.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

}
