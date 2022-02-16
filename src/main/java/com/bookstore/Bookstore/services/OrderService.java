package com.bookstore.Bookstore.services;

import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.entities.Item;
import com.bookstore.Bookstore.entities.Order;
import com.bookstore.Bookstore.entities.StatusComanda;
import com.bookstore.Bookstore.repositories.BookRepository;
import com.bookstore.Bookstore.repositories.OrderRepository;
import com.bookstore.Bookstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Configuration
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    public List<Order> getOrdersByIdClient(Long idClient) {
        log.info("getOrdersByIdClient");
        return orderRepository.findOrderByIdClient(idClient);
    }

    public List<Order> getAllOrders() {
        log.info("getAllOrders");
        return orderRepository.findAll();
    }

    public Order addBookToCart(Long idClient, Item item) {
        Book book = bookRepository.findByISBN(item.getISBN());
        Order order = orderRepository.findOrderByIdClientAndStatus(idClient, StatusComanda.initializata);

        /* Daca am pe stoc cantitatea solicitata */
        log.info(item.getQuantity());
        if (book.getStoc() >= item.getQuantity()) {
            /* Daca utilizatorul nu are o comanda initializatia */
            if (order == null) {
                List<Item> items = new ArrayList<>();
                items.add(item);
                Order newOrder = new Order( new Date(), items, StatusComanda.initializata, idClient);
                return orderRepository.insert(newOrder);
            }
            /* Daca utilizatorul are deja un cos activ */
            List<Item> newItems = order.getItems();
            int ok=1;
            for (Item newItem : newItems) {
                if (newItem.getISBN().equals(item.getISBN())) {
                    newItem.setQuantity(newItem.getQuantity() + item.getQuantity());
                    ok = 0;
                }
            }
            if(ok == 1) {
                newItems.add(item);
                order.setItems(newItems);
            }
            return orderRepository.save(order);
        }
        log.info("Nu am destul stoc");
        return null;
    }

    public Order confirmOrder(Long idClient) {
        Order order = orderRepository.findOrderByIdClientAndStatus(idClient, StatusComanda.initializata);
        for(Item item: order.getItems()) {
            Book book = bookRepository.findByISBN(item.getISBN());
            if (book.getStoc() >= item.getQuantity()) {
                book.setStoc(book.getStoc()-item.getQuantity());
                bookRepository.save(book);
            }
        }
        order.setStatus(StatusComanda.finalizata);
        return orderRepository.save(order);
    }

    public Order getOrderByIdClientAndStatus(Long idClientl, StatusComanda statusComanda) {
        log.info("getOrderByEmailAndStatus");
        return orderRepository.findOrderByIdClientAndStatus(idClientl, statusComanda);
    }
}
