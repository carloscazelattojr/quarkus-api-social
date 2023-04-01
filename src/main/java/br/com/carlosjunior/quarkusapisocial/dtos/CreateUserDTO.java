package br.com.carlosjunior.quarkusapisocial.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateUserDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotNull(message = "Ano é obrigatório")
    private Integer age;

}
