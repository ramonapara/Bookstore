package com.bookstore.Bookstore.controllers;

import com.bookstore.Bookstore.assembler.ItemDTOAssembler;
import com.bookstore.Bookstore.assembler.OrderDTOAssembler;
import com.bookstore.Bookstore.dto.ItemDTO;
import com.bookstore.Bookstore.dto.OrderDTO;
import com.bookstore.Bookstore.entities.Book;
import com.bookstore.Bookstore.entities.Item;
import com.bookstore.Bookstore.entities.Order;
import com.bookstore.Bookstore.entities.StatusComanda;
import com.bookstore.Bookstore.repositories.OrderRepository;
import com.bookstore.Bookstore.security.VerifyHeader;
import com.bookstore.Bookstore.services.OrderService;
import com.bookstore.Bookstore.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/bookcollection/orders")
@Log4j2
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    @Autowired
    VerifyHeader verifyHeader;
    @Autowired
    ItemDTOAssembler itemDTOAssembler;
    @Autowired
    OrderDTOAssembler orderDTOAssembler;

    @CrossOrigin
    @GetMapping(path = "/viewCart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOrdersByIdClientAndStatus( @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("call getOrdersByIdClientAndStatus");

        // Verificare autentificare
        if (authorizationHeader != null) {
           // VerifyHeader verifyHeader = new VerifyHeader();
            ResponseEntity<?> responseEntity = verifyHeader.isTokenValid(authorizationHeader);

            // Verificare token valid
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String body = (String) responseEntity.getBody();
                String role = body.split("-")[0];
                String username = body.split("-")[1];

                log.info(role);

                // Doar clientul isi poate vizualiza cosul
                if (role.equals("CLIENT")) {
                    log.info("Utilizatorul are rolul client");
                    // Adaugare link pentru confrmarea comeznii
                    Order order = orderService.getOrderByIdClientAndStatus(userService.getUserByUsername(username).getId(), StatusComanda.initializata);
                    OrderDTO orderDTO = orderDTOAssembler.toModel(order);
                    orderDTO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).confirmOrder(authorizationHeader)).withRel("Confirm-Order"));

                    // Adaugare link pentru fiecare carte din cos
                    List<ItemDTO> itemsDtoList = orderDTO.getItems();
                    List<Item> items = order.getItems();
                    int index = 0;
                    for (ItemDTO item : itemsDtoList) {
                        item.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getBookByISBN(items.get(index).getISBN(), Optional.of("true"))).withSelfRel());
                        index++;
                    }

                    return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
                }
                else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
    }
    @CrossOrigin
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllOrders() {
        log.info("call getAllOrders");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }
    @CrossOrigin
    @PostMapping(path = "/addToCart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToCart(@RequestBody Item item, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("call addToCart");
        if (authorizationHeader != null) {
          //  VerifyHeader verifyHeader = new VerifyHeader();
            ResponseEntity<?> responseEntity = verifyHeader.isTokenValid(authorizationHeader);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String body = (String) responseEntity.getBody();
                String role = body.split("-")[0];
                String username = body.split("-")[1];
                log.info(role);

                if (role.equals("CLIENT")) {
                    return ResponseEntity.status(HttpStatus.OK).body(orderService.addBookToCart(
                            userService.getUserByUsername(username).getId(),
                            item));
                }
                else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
    }

    @CrossOrigin
    @PutMapping(path = "/confirmOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmOrder(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("call confirmOrder");
        // Verificare autentificare
        if (authorizationHeader != null) {
      //`      VerifyHeader verifyHeader = new VerifyHeader();
            ResponseEntity<?> responseEntity = verifyHeader.isTokenValid(authorizationHeader);

            // Verificare token valid
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String body = (String) responseEntity.getBody();
                String role = body.split("-")[0];
                String username = body.split("-")[1];

                // Doar clientul isi poate vizualiza cosul
                if (role.equals("CLIENT")) {
                    log.info("ESTE CLIENTTTTTTTTTTTTTTT");
                    return ResponseEntity.status(HttpStatus.OK).body(orderService.confirmOrder(userService.getUserByUsername(username).getId()));
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
            }
        }
        else {
            log.info("N-am header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
        return null;
    }
}
