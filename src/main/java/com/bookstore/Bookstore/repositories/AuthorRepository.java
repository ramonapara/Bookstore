package com.bookstore.Bookstore.repositories;

import com.bookstore.Bookstore.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByNumeContains(String nume);
    List<Author> findByNume(String nume);
}
