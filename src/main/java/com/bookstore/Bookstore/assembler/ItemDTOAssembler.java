package com.bookstore.Bookstore.assembler;

import com.bookstore.Bookstore.controllers.OrderController;
import com.bookstore.Bookstore.dto.ItemDTO;
import com.bookstore.Bookstore.entities.Item;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ItemDTOAssembler extends RepresentationModelAssemblerSupport<Item, ItemDTO> {
    public ItemDTOAssembler() {
        super(OrderController.class, ItemDTO.class);
    }

    @Override
    public ItemDTO toModel(Item entity) {
        ItemDTO itemDTO = instantiateModel(entity);
        itemDTO.setISBN(entity.getISBN());
        itemDTO.setTitle(entity.getTitle());
        itemDTO.setPrice(entity.getPrice());
        itemDTO.setQuantity(entity.getQuantity());
        return itemDTO;
    }
}
