package com.movieflix.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
//@Table(name = "refreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer tokenId;

    @Column(nullable = false)
    @NotBlank(message = "Please provide an refreshToken")
    private String refreshToken;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private Instant expirationTime;

}
