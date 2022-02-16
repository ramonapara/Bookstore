package com.bookstore.Bookstore.entities;

public class PartialBook {
    private String ISBN;
    private String titlu;
    private String genLiterar;

    public PartialBook(String ISBN, String titlu, String genLiterar) {
        this.ISBN = ISBN;
        this.titlu = titlu;
        this.genLiterar = genLiterar;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getGenLiterar() {
        return genLiterar;
    }

    public void setGenLiterar(String genLiterar) {
        this.genLiterar = genLiterar;
    }
}
