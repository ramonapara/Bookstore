package com.bookstore.Bookstore.controllers;

import com.bookstore.Bookstore.entities.Author;
import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.services.BookAuthorService;
import com.bookstore.Bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookcollection/books")
@Log4j2
public class BookAuthorController {
    private final BookAuthorService bookAuthorService;
    @CrossOrigin
    @GetMapping(path = "/{ISBN}/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> getAllAuthorsByISBN(@PathVariable String ISBN) {
        log.info("call getAllAuthorsByISBN");
        return ResponseEntity.status(HttpStatus.OK).body(bookAuthorService.getAuthorsByIsbn(ISBN));
    }

    @CrossOrigin
    @GetMapping(path = "/{id}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getAllBooksByAuthorId(@PathVariable Integer id) {
        log.info("call getAllBooksByAuthorId");
        List<Book> bookList = bookAuthorService.getBooksByAutorId(id);

        /* Parcurgere in lista de carti din pagina: legatura self si legatura pentru autori*/
        int index = 0;
        for (Book book: bookList) {
            book.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getBookByISBN(bookList.get(index).getISBN(), Optional.of("true"))).withSelfRel());
            book.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookAuthorController.class).getAllAuthorsByISBN(bookList.get(index).getISBN())).withRel("authors"));
            index ++;
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookAuthorService.getBooksByAutorId(id));
    }

}
