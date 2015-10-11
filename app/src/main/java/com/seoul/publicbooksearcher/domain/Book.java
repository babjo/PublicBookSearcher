package com.seoul.publicbooksearcher.domain;

public class Book {

    private String bookId;
    private String title;
    private String library;
    private int statusCode;
    private String callNumber;

    // 서울시용
    private String locationCode;

    public Book(String title) {
        this.title = title;
    }

    public Book(String title, String library, int statusCode, String bookId) {
        this.bookId = bookId;
        this.title = title;
        this.library = library;
        this.statusCode = statusCode;
    }

    public Book(String title, String library, int statusCode, String bookId, String callNumber) {
        this.bookId = bookId;
        this.title = title;
        this.library = library;
        this.statusCode = statusCode;
        this.callNumber = callNumber;
    }

    public Book(String title, String library, int statusCode, String bookId, String callNumber, String locationCode) {
        this.bookId = bookId;
        this.title = title;
        this.library = library;
        this.statusCode = statusCode;
        this.callNumber = callNumber;
        this.locationCode = locationCode;
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
    public String getLocationCode() {return locationCode;}
    public void setLocationCode(String locationCode) {this.locationCode = locationCode;}
    public String getCallNumber() {return callNumber;}
    public void setCallNumber(String callNumber) {this.callNumber = callNumber;}
}
