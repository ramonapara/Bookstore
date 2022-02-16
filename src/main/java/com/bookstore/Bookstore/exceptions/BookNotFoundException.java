package com.bookstore.Bookstore.exceptions;


public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String message) {
        super(message);
    }
}
