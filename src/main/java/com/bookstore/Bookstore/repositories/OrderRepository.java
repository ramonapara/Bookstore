package com.bookstore.Bookstore.repositories;

import com.bookstore.Bookstore.entities.Order;
import com.bookstore.Bookstore.entities.StatusComanda;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findOrderByIdClient(Long idClient);
    Order findOrderByIdClientAndStatus(Long idClient, StatusComanda statusComanda);
}
