package com.bookstore.Bookstore.services;

import com.bookstore.Bookstore.entities.User;
import com.bookstore.Bookstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Configuration
public class UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: " + username);
        }

        /* Rolul utilizatorului meu */
    /*
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        /* Spring user */
    /*
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
    */
    public User createUser(User user) {
        log.info("createUser");
        /* Salvez utilizatorul cu parola deja criptata */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        log.info("getUserByUsername");
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        log.info("getAllUsers");
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        log.info("deleteUserById");
        userRepository.deleteById(id);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        log.info("getUserByUsernameAndPassword");
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
