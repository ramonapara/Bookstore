package com.bookstore.Bookstore.dto;

import com.bookstore.Bookstore.entities.BookAuthor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class BookDTO extends RepresentationModel<BookDTO> {
    private String ISBN;
    private String titlu;
    private String editura;
    private Integer anPublicare;
    private String genLiterar;
    private Integer stoc;
    private Integer pret;
}
