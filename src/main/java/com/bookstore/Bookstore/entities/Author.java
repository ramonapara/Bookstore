package com.bookstore.Bookstore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "autor", indexes = {@Index(columnList = "prenume"),
                                  @Index(columnList = "nume")})
public class Author extends RepresentationModel<Author> {
    @Id
    @Column(name = "id")
    private Integer idAutor;

    @Column(name = "prenume", unique = true)
    @NotNull(message = "Prenumele nu poate fi null")
    @Size(min = 1, max = 100, message = "Prenumele trebuie sa fie intre 1 si 100 de caractere")
    private String prenume;

    @Column(name = "nume")
    @NotNull(message = "Numele nu poate fi nula")
    @Size(min = 1, max = 1000, message = "Numele trebuie sa fie intre 1 si 1000 de caractere")
    private String nume;

    @JsonIgnore
    @OneToMany(mappedBy = "autorId")
    Set<BookAuthor> bookAuthors;
}
