package com.movieflix.exceptions;

public class FileAlreadyExistingException extends Exception {
    public FileAlreadyExistingException(String message) {
            super(message);
    }
}
