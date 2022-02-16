package com.bookstore.Bookstore.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data // Pentru getter/setter/constructor/toString
@Document(collation = "clienti")
public class UserProfile {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
