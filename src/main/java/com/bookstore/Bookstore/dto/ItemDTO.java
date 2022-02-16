package com.bookstore.Bookstore.dto;

import com.bookstore.Bookstore.entities.Item;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ItemDTO extends RepresentationModel<ItemDTO> {
    private String ISBN;
    private String title;
    private int price;
    private int quantity;
}
