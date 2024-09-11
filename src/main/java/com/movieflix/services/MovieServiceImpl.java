package com.movieflix.services;

import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import com.movieflix.entities.Movie;
import com.movieflix.exceptions.FileAlreadyExistingException;
import com.movieflix.exceptions.MovieNotFoundException;
import com.movieflix.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final FileService fileService;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException, FileAlreadyExistingException {

        //1.Upload File
        if(Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))){
            throw new FileAlreadyExistingException("File Already Exists with same name");
        }
        String UploadedfileName = fileService.uploadFile(path, file);

        //2.SEt the vaule of feild poster as fileName
        movieDto.setPoster(UploadedfileName);

        //3.map dto to movie object
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //4.Save the movie object -> saved Movie object
        Movie savedMovie = movieRepository.save(movie);

        //5. generate the poster url
         String posterUrl = baseUrl + "/file/" + UploadedfileName;

        //6. Map Movie object to dto object and return it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) throws MovieNotFoundException {
        //1. check the data in DB and if exists, fetch the data of the given ID.

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie Not Found with id : " + movieId));

        //2. generate poster
        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        //3.map to MovieDto object and return it.
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }
    @Override
    public List<MovieDto> getAllMovies() {
        //1. fetch all movies from db
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();

        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        //2.iterate throug hlist gemerate poster url for each movie obj, and map to MovieDto obj
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException, MovieNotFoundException {
        //1.Check the movie object exists with given movieId
        Movie mv = movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie Not Found with id : " + movieId));

        //2.check if the file is null, do nothing
        //If file is not null delete existing file associated with the record.
        String fileName = mv.getPoster();
        if(file != null){
            Files.deleteIfExists(Paths.get(path+File.separator+fileName));
            fileName = fileService.uploadFile(path,file);
        }

        //3.set movieDto's poster vaule , according to step 2
        movieDto.setPoster(fileName);

        //4. map it to movie object
        Movie movie = new Movie(
                mv.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        //5.save the movie object -> return saved movie object
        movieRepository.save(movie);
        //6.generate poster url
        String posterUrl = baseUrl + "/file/" + movie.getPoster();
        //7.map to movieDto and return it.
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException, MovieNotFoundException {

        //1. check if movie object exists in db
        Movie mv = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie doesnt exist"));
        Integer id = mv.getMovieId();
        //2. delete the file associate with the object
        Files.deleteIfExists(Paths.get(path+File.separator+mv.getPoster()));

        //3. delete the movie  object
        movieRepository.delete(mv);
        return "Movie Deleted with ID" + id;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();

        for (Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }

        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                moviePages.getTotalElements(),
                    moviePages.getTotalPages(),
                    moviePages.isLast());
        }

        @Override
        public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
            Sort sort = dir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Movie> moviePages = movieRepository.findAll(pageable);
            List<Movie> movies = moviePages.getContent();

            List<MovieDto> movieDtos = new ArrayList<>();

            for (Movie movie : movies) {
                String posterUrl = baseUrl + "/file/" + movie.getPoster();
                MovieDto movieDto = new MovieDto(
                        movie.getMovieId(),
                        movie.getTitle(),
                        movie.getDirector(),
                        movie.getStudio(),
                        movie.getMovieCast(),
                        movie.getReleaseYear(),
                        movie.getPoster(),
                        posterUrl
                );
                movieDtos.add(movieDto);
            }

            return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                                         moviePages.getTotalElements(),
                                         moviePages.getTotalPages(),
                                         moviePages.isLast());

    }
}
