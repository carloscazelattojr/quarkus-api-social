package br.com.carlosjunior.quarkusapisocial.repositories;


import br.com.carlosjunior.quarkusapisocial.entities.Follower;
import br.com.carlosjunior.quarkusapisocial.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean existsFollows(User follower, User user) {
        Map<String, Object> params = Parameters.with("follower", follower).and("user", user).map();
        PanacheQuery<Follower> query = find("follower = :follower and user = :user ", params);
        Optional<Follower> result = query.firstResultOptional();
        return result.isPresent();
    }


    public List<Follower> findByUser(Long userId) {
        PanacheQuery<Follower> query = find("user.id", userId);
        return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        Map<String, Object> params = Parameters.with("followerId", followerId).and("userId", userId).map();
        delete("follower.id = :followerId and user.id = :userId", params);
    }
}
