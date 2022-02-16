package com.bookstore.Bookstore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.springframework.hateoas.RepresentationModel;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carte", indexes = {@Index(columnList = "an_publicare"),
                                  @Index(columnList = "gen_literar")})
public class Book extends RepresentationModel<Book>{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ISBN", unique = true)
    private String ISBN;

    @Column(name = "titlu", unique = true)
    @NotNull(message = "Titlul nu poate fi null")
    @Size(min = 1, max = 100, message = "Titlul trebuie sa fie intre 1 si 100 de caractere")
    private String titlu;

    @Column(name = "editura")
    @NotNull(message = "Editura nu poate fi nula")
    @Size(min = 1, max = 1000, message = "Editura trebuie sa fie intre 1 si 1000 de caractere")
    private String editura;

    @Column(name = "an_publicare")
    @NotNull(message = "Anul de publicare nu poate fi null")
    @Min(value = 0, message = "Anul de publicare nu poate fi mai mic decat 0")
    private Integer anPublicare;

    @Column(name = "gen_literar")
    @NotNull(message = "Genul literar nu poate fi null")
    @Size(min = 1, max = 1000, message = "Genul literar trebuie sa fie intre 1 si 1000 de caractere")
    private String genLiterar;

    @Column(name = "stoc")
    @Min(value = 0, message = "Stocul nu poate fi negativ")
    private Integer stoc;

    @Column(name = "pret")
    @NotNull(message = "Pretul nu poate fi null")
    @Min(value = 0, message = "Pretul nu poate fi mai mic de 0 lei")
    private Integer pret;

    @JsonIgnore
    @OneToMany(mappedBy = "carteISBN")
    Set<BookAuthor> bookAuthors;

}

