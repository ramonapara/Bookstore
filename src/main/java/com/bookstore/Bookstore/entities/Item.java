package com.bookstore.Bookstore.entities;

import lombok.Data;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

public class Item implements Serializable {
    private String ISBN;
    private String title;

    public Item(String ISBN, String title, int price, int quantity) {
        this.ISBN = ISBN;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    private int price;
    private int quantity;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
