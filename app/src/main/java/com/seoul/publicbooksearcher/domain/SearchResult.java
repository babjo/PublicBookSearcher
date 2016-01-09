package com.seoul.publicbooksearcher.domain;

import java.util.List;

public class SearchResult {

    private Library library;

    public SearchResult(Library library) {
        this.library = library;
    }

    public List<Book> getBooks() {
        return library.getBooks();
    }

    public Long getLibraryId() {return library.getId();}

}
