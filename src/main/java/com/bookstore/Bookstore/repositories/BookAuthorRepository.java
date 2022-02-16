package com.bookstore.Bookstore.repositories;

import com.bookstore.Bookstore.entities.Author;
import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.entities.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    @Query(value = "SELECT * FROM autor au, autorcarte ba WHERE ba.carte_isbn=:ISBN AND au.id=ba.autor_id", nativeQuery = true)
    List<Object[]> findAuthorsByIsbn(@Param("ISBN") String ISBN);
    @Query(value = "SELECT * FROM carte ca, autorcarte ba WHERE ba.autor_id=:id AND ca.ISBN=ba.carte_isbn", nativeQuery = true)
    List<Object[]> findBooksByAutorId(@Param("id") Integer id);

}
