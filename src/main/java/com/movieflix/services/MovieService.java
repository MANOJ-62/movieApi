package com.movieflix.services;

import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import com.movieflix.exceptions.FileAlreadyExistingException;
import com.movieflix.exceptions.MovieNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException, FileAlreadyExistingException;
    MovieDto getMovie(Integer movieId) throws MovieNotFoundException;
    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException, MovieNotFoundException;

    String deleteMovie (Integer movieId) throws IOException, MovieNotFoundException;

    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);
    MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize,String sortby, String sortDir);

}
