package com.seoul.publicbooksearcher.presentation.presenter;

import com.seoul.publicbooksearcher.domain.Book;

import java.util.List;

public interface SearchBooksPresenter {

    void searchBefore();
    void searchCompleted(List<Book> books);

}
