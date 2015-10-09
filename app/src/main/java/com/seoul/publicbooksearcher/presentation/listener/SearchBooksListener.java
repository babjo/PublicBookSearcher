package com.seoul.publicbooksearcher.presentation.listener;

import com.seoul.publicbooksearcher.domain.Book;

import java.util.List;

public interface SearchBooksListener {

    void searchBefore();
    void searchCompleted(List<Book> books);

}
