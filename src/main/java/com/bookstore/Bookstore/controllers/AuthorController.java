package com.bookstore.Bookstore.controllers;

import com.bookstore.Bookstore.entities.Author;
import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.services.AuthorService;
import com.bookstore.Bookstore.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/bookcollection")
@Log4j2
public class AuthorController {
    private final AuthorService authorService;

    @CrossOrigin
    @GetMapping(path = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAuthors(@RequestParam("name") Optional<String> name, @RequestParam("match") Optional<String> match) {
        log.info("call getAllAuthors");
        List<Author> authorFilter = null;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        /* Cautare partiala dupa numele de familie al autorului */
        if (name.isPresent()) {
            authorFilter = authorService.getAuthorsLike(name.get());

            if (authorFilter != null) {
                httpStatus = HttpStatus.OK;
            }
        }

        /* Cautare exacta dupa numele de familie al autorgului */
        if (name.isPresent() && match.isPresent()) {
            authorFilter = authorService.getAuthors(name.get());

            if (authorFilter != null) {
                httpStatus = HttpStatus.OK;
            }
        }

        /* Parcurgere in lista de carti din pagina: legatura self si legatura pentru autori*/
        int index = 0;
        for (Author author: authorFilter) {
            author.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAuthorById(authorFilter.get(index).getIdAutor())).withSelfRel());
            author.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookAuthorController.class).getAllBooksByAuthorId(authorFilter.get(index).getIdAutor())).withRel("books"));
            index ++;
        }
        return new ResponseEntity<>(authorFilter,httpStatus);
    }

    @CrossOrigin
    @GetMapping(path = "/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthorById(@PathVariable Integer id) {
        log.info(String.format("call getAuthorById, id: %s", id.toString()));
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthorById(id));
    }

    /* HTTP Options - Expunerea informatiilor  despre resurse
   Clientul poate specifica un URL pentru OPTIONS */
    @RequestMapping(value = "/authors", method = RequestMethod.OPTIONS)
    ResponseEntity<?> collectionOptions() throws IOException {
        ResponseEntity responseEntity = null;

        byte[] reportBytes = null;
        String filename = "OpenApiAuthors.json";
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
