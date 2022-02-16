package com.bookstore.Bookstore.repositories;

import com.bookstore.Bookstore.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,String> {
    Book findByISBN(String ISBN);
    List<Book> findByGenLiterar(String genLiterar);
    List<Book> findByAnPublicare(Integer anPublicare);
    List<Book> findByGenLiterarAndAnPublicare(String genLiterar, Integer anPublicare);
}
