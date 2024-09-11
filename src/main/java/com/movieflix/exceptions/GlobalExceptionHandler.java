package com.movieflix.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileAlreadyExistingException.class)
    public ProblemDetail handleFileAlreadyExistingException(FileAlreadyExistingException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(FileEmptyException.class)
    public ProblemDetail handleFileEmptyException(FileEmptyException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ProblemDetail handleFileAlreadyExistingException(MovieNotFoundException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }
}
