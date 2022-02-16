package com.bookstore.Bookstore.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Log4j2
public class CustomExceptionHandler {
    @ResponseBody
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> bookNotFoundHandler(BookNotFoundException bookNotFoundException) {
        log.error("thrown BookNotFoundException");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Book with ISBN %s not found",
                bookNotFoundException.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> authorNotFoundHandler(AuthorNotFoundException authorNotFoundException) {
        log.error("thrown AuthorNotFoundException");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Author with id %s not found",
                authorNotFoundException.getMessage()));
    }
}
