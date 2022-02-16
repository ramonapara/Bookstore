package com.bookstore.Bookstore.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@Data // Pentru getter/setter/constructor/toString
@Document(collection = "comenzi")
public class Order implements Serializable {
    @Id
    private String id;
    private Date date;
    private List<Item> items;
    private StatusComanda status;
    private Long idClient;

    public Order(String id, Date date, List<Item> items, StatusComanda status, Long idClient) {
        this.id = id;
        this.date = date;
        this.items = items;
        this.status = status;
        this.idClient = idClient;
    }

    public Order(Date date, List<Item> items, StatusComanda status, Long idClient) {
        this.date = date;
        this.items = items;
        this.status = status;
        this.idClient = idClient;
    }

}
