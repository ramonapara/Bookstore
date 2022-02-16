package com.bookstore.Bookstore.services;

import com.bookstore.Bookstore.entities.Author;
import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.exceptions.AuthorNotFoundException;
import com.bookstore.Bookstore.exceptions.BookNotFoundException;
import com.bookstore.Bookstore.repositories.AuthorRepository;
import com.bookstore.Bookstore.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Configuration
public class AuthorService {
    private final AuthorRepository authorRepository;;

    public List<Author> getAllAuthors() {
        log.info("getAllAuthors");
        return authorRepository.findAll();
    }

    public Author getAuthorById(Integer id) {
        log.info("getAuthorById");
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id.toString()));
    }

    public List<Author> getAuthorsLike(String nume) {
        log.info("getAuthorsLike");
        return authorRepository.findByNumeContains(nume);
    }

    public List<Author> getAuthors(String nume) {
        log.info("getAuthors");
        return authorRepository.findByNume(nume);
    }
}
