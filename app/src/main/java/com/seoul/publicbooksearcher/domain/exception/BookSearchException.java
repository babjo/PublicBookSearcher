package com.seoul.publicbooksearcher.domain.exception;


public class BookSearchException extends RuntimeException{

    private Long libraryId;

    public BookSearchException(String detailMessage, Long libraryId) {
        super(detailMessage);
        this.libraryId = libraryId;
    }

    public Long getLibraryId() {
        return libraryId;
    }
}
