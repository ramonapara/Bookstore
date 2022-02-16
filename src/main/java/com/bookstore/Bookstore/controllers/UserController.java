package com.bookstore.Bookstore.controllers;

import com.bookstore.Bookstore.entities.User;
import com.bookstore.Bookstore.security.VerifyHeader;
import com.bookstore.Bookstore.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/bookcollection/users")
@Log4j2
public class UserController {
    private final UserService userService;
    @Autowired
    VerifyHeader verifyHeader;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("call getAllUsers");

        // Doar ADMIN ul poate accesa
        // Verificare autentificare
        if (authorizationHeader != null) {
            ResponseEntity<?> responseEntity = verifyHeader.isTokenValid(authorizationHeader);

            // Verificare token valid
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String body = (String) responseEntity.getBody();
                String role = body.split("-")[0];
                String username = body.split("-")[1];

                // Doar clientul isi poate vizualiza cosul
                if (role.equals("ADMIN")) {
                    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
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

    @CrossOrigin
    @GetMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        log.info("call getUserByUsername");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUsername(username));
    }

    @CrossOrigin
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        log.info("call addUser");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @CrossOrigin
    @GetMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("call deleteUser");

        // Doar ADMIN ul poate accesa
        // Verificare autentificare
        if (authorizationHeader != null) {
            ResponseEntity<?> responseEntity = verifyHeader.isTokenValid(authorizationHeader);

            // Verificare token valid
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String body = (String) responseEntity.getBody();
                String role = body.split("-")[0];
                String username = body.split("-")[1];

                // Doar clientul isi poate vizualiza cosul
                if (role.equals("ADMIN")) {
                    userService.deleteUserById(id);
                    return ResponseEntity.status(HttpStatus.OK).body("");
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
}
