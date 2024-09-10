package com.movieflix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Integer movieId;

    @NotBlank(message = "Please Provide movie's title")
    private String title;

    @NotBlank(message = "Please Provide director's name")
    private String director;

    @NotBlank(message = "Please Provide studio's name")
    private String studio;

    private Set<String> movieCast;

    private Integer releaseYear;

    @NotBlank(message = "Please Provide poster")
    private String poster;

    @NotBlank(message = "Please Provide poster's Url")
    private String posterUrl;
}
