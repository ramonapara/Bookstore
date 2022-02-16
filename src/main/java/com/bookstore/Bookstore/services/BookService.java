package com.bookstore.Bookstore.services;

import com.bookstore.Bookstore.entities.Author;
import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.exceptions.BookNotFoundException;
import com.bookstore.Bookstore.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Configuration
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        log.info("getAllBooks");
        return bookRepository.findAll();
    }

    public Book getBookByISBN(String ISBN) {
        log.info("getBookByISBN");
        return bookRepository.findById(ISBN).orElseThrow(() -> new BookNotFoundException(ISBN));
    }

    public List<Book> printPaginationBooks(int page, int items_per_page)
    {
        log.info("printPaginationBooks");
        List<Book> books = bookRepository.findAll();
        int nrPagini = books.size()/items_per_page;

        if (nrPagini >= page) {
            books = books.subList(items_per_page*(page-1),items_per_page*page);
            return books;
        }
        return null;
    }

    public Book addBook(Book book) {
        log.info("addBook");
        book.setISBN(book.getISBN());
        return bookRepository.save(book);
    }

    public List<Book> getBookByGenLiterar(String genLiterar) {
        log.info("getBookByGenre");
        return bookRepository.findByGenLiterar(genLiterar);
    }

    public List<Book> getBookByAnPublicare(Integer anPublicare) {
        log.info("getBookByAnPublicare");
        return bookRepository.findByAnPublicare(anPublicare);
    }

    public List<Book> getBookByGenLiterarAndAnPublicare(String genLiterar, Integer anPublicare) {
        log.info("getBookByGenLiterarAndAnPublicare");
        return bookRepository.findByGenLiterarAndAnPublicare(genLiterar, anPublicare);
    }
}
