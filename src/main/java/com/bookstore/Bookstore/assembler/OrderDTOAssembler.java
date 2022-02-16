package com.bookstore.Bookstore.assembler;

import com.bookstore.Bookstore.controllers.OrderController;
import com.bookstore.Bookstore.dto.ItemDTO;
import com.bookstore.Bookstore.dto.OrderDTO;
import com.bookstore.Bookstore.entities.Item;
import com.bookstore.Bookstore.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDTOAssembler extends RepresentationModelAssemblerSupport<Order, OrderDTO> {
    @Autowired
    ItemDTOAssembler itemDTOAssembler;

    public OrderDTOAssembler() {
        super(OrderController.class, OrderDTO.class);
    }

    @Override
    public OrderDTO toModel(Order entity) {
        OrderDTO orderDTO = instantiateModel(entity);
        orderDTO.setId(entity.getId());
        orderDTO.setDate(entity.getDate());
        orderDTO.setStatus(entity.getStatus());
        orderDTO.setIdClient(entity.getIdClient());

        List<ItemDTO> listItems = new ArrayList<ItemDTO>();
        for (Item item: entity.getItems()) {
            ItemDTO itemDTO = itemDTOAssembler.toModel(item);
            listItems.add(itemDTO);
        }
        orderDTO.setItems(listItems);
        return orderDTO;
    }
}
