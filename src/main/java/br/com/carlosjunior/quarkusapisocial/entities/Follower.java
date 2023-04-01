package br.com.carlosjunior.quarkusapisocial.entities;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tb_followers")
public class Follower {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;


}
