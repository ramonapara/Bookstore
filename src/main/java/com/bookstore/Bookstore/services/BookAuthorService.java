package com.bookstore.Bookstore.services;

import com.bookstore.Bookstore.entities.Author;
import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.exceptions.AuthorNotFoundException;
import com.bookstore.Bookstore.repositories.AuthorRepository;
import com.bookstore.Bookstore.repositories.BookAuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Configuration
public class BookAuthorService {
    private final BookAuthorRepository bookAuthorRepository;
    private final AuthorService authorService;
    private final BookService bookService;

    public List<Author> getAuthorsByIsbn(String ISBN) {
        log.info("getAuthorsByIsbn");
        List<Object[]> resList = bookAuthorRepository.findAuthorsByIsbn(ISBN);
        List<Author> authorList = new ArrayList<Author>();
        log.info("Am trecut de initializare");
        for (Object[] author: resList) {
            log.info("Id autor: " + author[0]);
            log.info("Prenume: " + author[1]);
            log.info("Nume: " + author[2]);
            authorList.add(authorService.getAuthorById((Integer) author[0]));
        }
        return authorList;
    }

    public List<Book> getBooksByAutorId(Integer id) {
        log.info("getBooksByAutorId");
        List<Object[]> resList = bookAuthorRepository.findBooksByAutorId(id);
        List<Book> bookList = new ArrayList<Book>();

        for (Object[] book: resList) {
            bookList.add(bookService.getBookByISBN((String) book[0]));
        }
        return bookList;
    }
}
