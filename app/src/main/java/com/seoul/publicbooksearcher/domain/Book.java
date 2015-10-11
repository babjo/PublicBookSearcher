package com.seoul.publicbooksearcher.domain;

public class Book {

    private String bookId;
    private String title;
    private String library;
    private int statusCode;

    public Book(String title) {
        this.title = title;
    }

    public Book(String title, String library, int statusCode, String bookId) {
        this.title = title;
        this.library = library;
        this.statusCode = statusCode;
        this.bookId = bookId;
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
    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;
    }
    public String getBookId() {return bookId;}
    public void setBookId(String bookId) {this.bookId = bookId;}
}
