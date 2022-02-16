package com.bookstore.Bookstore.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "autorcarte")
public class BookAuthor {
    @Id
    @Column(name = "numar_ordine")
    private Long numarOrdine;

    @ManyToOne
    @MapsId("carte_isbn")
    @JoinColumn(name = "carte_isbn")
    Book carteISBN;

    @ManyToOne
    @MapsId("autor_id")
    @JoinColumn(name = "autor_id")
    Author autorId;

}
