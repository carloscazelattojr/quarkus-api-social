package br.com.carlosjunior.quarkusapisocial.dtos;

import br.com.carlosjunior.quarkusapisocial.entities.Follower;
import lombok.Data;

@Data
public class FollowerResponse {

    private Long id;
    private String name;

    public FollowerResponse() {
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public FollowerResponse(Follower entity) {
        this(entity.getId(), entity.getFollower().getName());
    }


}
