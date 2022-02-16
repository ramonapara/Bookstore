package com.bookstore.Bookstore.controllers;

import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.entities.PartialBook;
import com.bookstore.Bookstore.security.VerifyHeader;
import com.bookstore.Bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/bookcollection")
@Log4j2
public class BookController {
    private final BookService bookService;
    @Autowired
    VerifyHeader verifyHeader;

    @CrossOrigin
    @GetMapping(path = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBooks(@RequestParam("page")Optional<Integer> page, @RequestParam(value = "items_per_page", defaultValue = "3") Optional<Integer> items_per_page) {
        log.info("call getAllBooks");
        HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

        /* Afisarea paginata a listei de carti */
        if (page.isPresent())
        {
            log.info("call printPaginationBooks");
            List<Book> pagination = bookService.printPaginationBooks(page.get(), items_per_page.get());

            if (pagination != null) {
                httpStatus = HttpStatus.OK;
                int index = 0;

                /* Parcurgere in lista de carti din pagina: legatura self si legatura pentru autori*/
                for (Book book: pagination) {
                    book.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getBookByISBN(pagination.get(index).getISBN(),Optional.of("true"))).withSelfRel());
                    book.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookAuthorController.class).getAllAuthorsByISBN(pagination.get(index).getISBN())).withRel("authors"));
                    if (book.getStoc() > 0) {
                        book.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).addBookToCart(book, null)).withRel("AddToCart"));
                    }
                    index ++;
                }

                /* Adaugare legaturi prev si next */
                CollectionModel<Book> navigatePagination = CollectionModel.of(pagination);
                navigatePagination.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBooks(page,items_per_page)).withSelfRel());
                if (page.get() != 1) {
                    navigatePagination.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBooks(Optional.of(page.get() - 1), items_per_page)).withRel("prev-page"));
                }
                if (page.get() != (bookService.getAllBooks().size()/items_per_page.get() + 1)) {
                    navigatePagination.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBooks(Optional.of(page.get()+1),items_per_page)).withRel("next-page"));
                }
                log.info("Ies din paginare");
                return new ResponseEntity<>(navigatePagination,httpStatus);
            }
        }
        return ResponseEntity.status(httpStatus).body(bookService.getAllBooks());
    }

    @CrossOrigin
    @GetMapping(path = "/books/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllFilterBooks(@RequestParam("genre")Optional<String> genre, @RequestParam("year") Optional<Integer> year) {
        log.info("call getAllFilterBooks");
        List<Book> booksFilter = null;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        /* Cautare dupa gen literar */
        if (genre.isPresent() && year.isEmpty()) {
            booksFilter = bookService.getBookByGenLiterar(genre.get());

            if (booksFilter != null) {
                httpStatus = HttpStatus.OK;
            }
        }

        /* Cautare dupa an */
        if (genre.isEmpty() && year.isPresent()) {
            booksFilter = bookService.getBookByAnPublicare(year.get());

            if (booksFilter != null) {
                httpStatus = HttpStatus.OK;
            }
        }

        /* Cautare dupa gen si an */
        if (genre.isPresent() && year.isPresent()) {
            booksFilter = bookService.getBookByGenLiterarAndAnPublicare(genre.get(),year.get());

            if (booksFilter != null) {
                httpStatus = HttpStatus.OK;
            }
        }

        /* Parcurgere in lista de carti din pagina: legatura self si legatura pentru autori*/
        int index = 0;
        for (Book book: booksFilter) {
            book.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getBookByISBN(booksFilter.get(index).getISBN(),Optional.of("true"))).withSelfRel());
            book.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookAuthorController.class).getAllAuthorsByISBN(booksFilter.get(index).getISBN())).withRel("authors"));
            index ++;
        }
        return new ResponseEntity<>(booksFilter,httpStatus);
    }
    @CrossOrigin
    @GetMapping(path = "/books/{ISBN}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBookByISBN(@PathVariable String ISBN, @RequestParam(value = "verbose")Optional<String> verbose) {
        log.info(String.format("call getBookByISBN, ISBN: %s", ISBN));

        if (verbose.isPresent()) {
            if (verbose.get().equals("false")) {
                Book book = bookService.getBookByISBN(ISBN);
                PartialBook partialBook = new PartialBook(book.getISBN(), book.getTitlu(), book.getGenLiterar());
                return ResponseEntity.status(HttpStatus.OK).body(partialBook);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookByISBN(ISBN));
    }

    @CrossOrigin
    @PostMapping(path = "/book")
    public ResponseEntity<?> addBook(@RequestBody Book book, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("call addBook");
        // Doar MANAGER ul poate accesa
        // Verificare autentificare
        if (authorizationHeader != null) {
            ResponseEntity<?> responseEntity = verifyHeader.isTokenValid(authorizationHeader);

            // Verificare token valid
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String body = (String) responseEntity.getBody();
                String role = body.split("-")[0];
                String username = body.split("-")[1];

                // Doar clientul isi poate vizualiza cosul
                if (role.equals("MANAGER")) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(book));
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
    }

    // To remove...Already in OrderController
    @CrossOrigin
    @PostMapping(path = "/book/addToCart")
    public ResponseEntity<?> addBookToCart(@RequestBody Book book, @RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        log.info("call addBookToCart");
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    /* HTTP Options - Expunerea informatiilor  despre resurse
       Clientul poate specifica un URL pentru OPTIONS */
    @RequestMapping(value = "/books", method = RequestMethod.OPTIONS)
    ResponseEntity<?> collectionOptions() throws IOException {
        ResponseEntity responseEntity = null;

        byte[] reportBytes = null;
        String filename = "OpenApiBooks.json";
        File result = new File("/home/ramona/Documents/Anul 4/POS/Bookstore/src/main/resources/" + filename);

        if (result.exists()) {
            log.info("Vad fisierul");
            InputStream inputStream = new FileInputStream("/home/ramona/Documents/Anul 4/POS/Bookstore/src/main/resources/" + filename);
            String type = result.toURL().openConnection().guessContentTypeFromName(filename);

            byte[] out = org.apache.commons.io.IOUtils.toByteArray(inputStream);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=" + filename);
            responseHeaders.add("Content-Type",type);

            responseEntity = new ResponseEntity(out, responseHeaders,HttpStatus.OK);
        }
        else {
            log.info("Nu vad fisierul");
            responseEntity = new ResponseEntity ("File Not Found", HttpStatus.OK);
        }
        return responseEntity;
    }

    /* HTTP Options - Expunerea informatiilor  despre resurse
   Clientul poate specifica un URL pentru OPTIONS */
    @RequestMapping(value = "/", method = RequestMethod.OPTIONS)
    ResponseEntity<?> collectionOptionsForAll() throws IOException {
        ResponseEntity responseEntity = null;

        byte[] reportBytes = null;
        String filename = "OpenAPIBookstore.json";
        File result = new File("/home/ramona/Documents/Anul 4/POS/Bookstore/src/main/resources/" + filename);

        if (result.exists()) {
            log.info("Vad fisierul");
            InputStream inputStream = new FileInputStream("/home/ramona/Documents/Anul 4/POS/Bookstore/src/main/resources/" + filename);
            String type = result.toURL().openConnection().guessContentTypeFromName(filename);

            byte[] out = org.apache.commons.io.IOUtils.toByteArray(inputStream);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=" + filename);
            responseHeaders.add("Content-Type",type);

            responseEntity = new ResponseEntity(out, responseHeaders,HttpStatus.OK);
        }
        else {
            log.info("Nu vad fisierul");
            responseEntity = new ResponseEntity ("File Not Found", HttpStatus.OK);
        }
        return responseEntity;
    }
}
