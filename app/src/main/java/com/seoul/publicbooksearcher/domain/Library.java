package com.seoul.publicbooksearcher.domain;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private String name;
    private List<Book> books;
    private Location location;

    public Library(String name, List<Book> books) {
        this.name = name;
        this.books = books;
    }

    public Library(String libraryName) {
        this.name = libraryName;
        this.books = new ArrayList();
    }

    public Library(String name, double latitude, double longitude) {
        this.name = name;
        this.location = new Location(latitude, longitude);
        this.books = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void add(Book book) {
        this.books.add(book);
    }

    public void clear() {
        this.books.clear();
    }

    public double distance(Location location){
        return this.location.distance(location);
    }
}
