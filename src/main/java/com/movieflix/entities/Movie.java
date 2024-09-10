package com.movieflix.entities;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable=false, length = 200)
    @NotBlank(message = "Please Provide movie's title")
    private String title;

    @Column(nullable=false)
    @NotBlank(message = "Please Provide director's name")
    private String director;

    @Column(nullable=false)
    @NotBlank(message = "Please Provide studio's name")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "Movie_cast")
    private Set<String> movieCast;

    @Column(nullable=false)
    private Integer releaseYear;

    @Column(nullable=false)
    @NotBlank(message = "Please Provide poster")
    private String poster;
}
