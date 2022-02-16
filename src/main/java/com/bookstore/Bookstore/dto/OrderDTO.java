package com.bookstore.Bookstore.dto;

import com.bookstore.Bookstore.entities.Item;
import com.bookstore.Bookstore.entities.StatusComanda;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.List;

@Data
public class OrderDTO extends RepresentationModel<OrderDTO> {
    private String id;
    private Date date;
    private List<ItemDTO> items;
    private StatusComanda status;
    private Long idClient;
}
