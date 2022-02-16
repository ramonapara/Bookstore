package com.bookstore.Bookstore.repositories;

import com.bookstore.Bookstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    void deleteById(Long id);
    User findByUsernameAndPassword(String username, String password);
}
