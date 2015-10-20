package com.seoul.publicbooksearcher.domain;

import java.util.List;

public class SearchResult {

    private List<Book> books;
    private String library;

    public SearchResult(String library, List<Book> books) {
        this.library = library;
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

}
