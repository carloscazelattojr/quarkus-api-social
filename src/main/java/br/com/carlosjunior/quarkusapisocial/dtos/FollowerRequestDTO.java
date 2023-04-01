package br.com.carlosjunior.quarkusapisocial.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FollowerRequestDTO {
    @NotNull(message = "Follower é obrigatório")
    private Long followerId;
}
