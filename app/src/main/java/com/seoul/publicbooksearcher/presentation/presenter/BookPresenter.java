package com.seoul.publicbooksearcher.presentation.presenter;

import android.os.Handler;
import android.util.Log;


import com.seoul.publicbooksearcher.domain.async_usecase.AsyncUseCase;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.usecase.UseCase;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;
import com.seoul.publicbooksearcher.presentation.view.component.BookListView;
import com.seoul.publicbooksearcher.presentation.view.component.BookTitleAutoCompleteTextView;
import com.seoul.publicbooksearcher.presentation.view.component.ProgressBarView;

import java.util.List;

public class BookPresenter {

    private final static String TAG = BookPresenter.class.getName();

    private final UseCase getRecentKeywords;
    private final UseCase addRecentKeyword;
    private final AsyncUseCase searchBooks;
    private final AsyncUseCase searchTitles;

    private final BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView2;
    private final BookListView bookListView2;
    private final ProgressBarView progressBarView;

    public BookPresenter(UseCase getRecentKeywords, UseCase addRecentKeyword, AsyncUseCase searchBooks, AsyncUseCase searchTitles, BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView2, BookListView bookListView2, ProgressBarView progressBarView) {

        this.getRecentKeywords = getRecentKeywords;
        this.addRecentKeyword = addRecentKeyword;
        this.searchBooks = searchBooks;
        this.searchTitles = searchTitles;

        this.bookTitleAutoCompleteTextView2 = bookTitleAutoCompleteTextView2;
        this.bookListView2 = bookListView2;
        this.progressBarView = progressBarView;
    }

    public void getRecentKeywords() {
        final List<String> keywords = (List) getRecentKeywords.execute(null);

        String keywordContents = "";
        for (String keyword : keywords)
            keywordContents += keyword + ", ";
        Log.i(TAG, "Recent Keyword : " + keywordContents);

        new Handler().postDelayed(new Runnable() { // new Handler and Runnable
            @Override
            public void run() {
                BookPresenter.this.bookTitleAutoCompleteTextView2.setTitles(keywords);
            }
        }, 500);

    }

    public void searchTitles(String keyword){
        searchTitles.execute(keyword, new AsyncUseCaseListener<Void, List<String>>() {
            @Override
            public void onBefore(Void beforeArgs) {}

            @Override
            public void onAfter(List<String> afterArg) {
                Log.i("UPDATE", "3");
                bookTitleAutoCompleteTextView2.setTitles(afterArg);
            }

            @Override
            public void onError(Exception e) {}
        });
    }

    public void searchBooks(String keyword) {
        Log.i(TAG, "entered keyword = " + keyword + "\n search start");
        addRecentKeyword.execute(keyword);
        Log.i(TAG, "addRecentKeyword = " + keyword);

        searchBooks.execute(keyword, new AsyncUseCaseListener<Void, List<Book>>() {
            @Override
            public void onBefore(Void Void) {
                bookTitleAutoCompleteTextView2.dismissDropDown();
                bookListView2.hideKeyboard();

                progressBarView.visible();
                bookListView2.clear();
            }

            @Override
            public void onAfter(List<Book> books) {
                progressBarView.gone();
                bookListView2.addAll(books);
            }

            @Override
            public void onError(Exception e) {}
        });
    }
}
