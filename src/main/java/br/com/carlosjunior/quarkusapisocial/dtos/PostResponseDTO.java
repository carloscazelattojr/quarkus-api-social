package br.com.carlosjunior.quarkusapisocial.dtos;

import br.com.carlosjunior.quarkusapisocial.entities.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDTO {
    private String text;
    private LocalDateTime dataTime;

    public static PostResponseDTO fromEntyt(Post entity){
       PostResponseDTO responseDTO = new PostResponseDTO();
       responseDTO.setText(entity.getPostText());
       responseDTO.setDataTime(entity.getDataTime());
       return responseDTO;
    }

}
