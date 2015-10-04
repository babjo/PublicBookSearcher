package com.seoul.publicbooksearcher;

public class Book {

    private String title;
    private String library;
    private int statusCode;

    public Book(String title) {
        this.title = title;
    }

    public Book(String title, String library, int statusCode) {
        this.title = title;
        this.library = library;
        this.statusCode = statusCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLibrary() {
        return library;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
