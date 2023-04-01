package br.com.carlosjunior.quarkusapisocial.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreatePostDTO {
    @NotBlank(message = "Texto é obrigatório")
    private String text;
}
