package com.seoul.publicbooksearcher.presentation.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    public static final String NOT_ONLINE_MSG = "인터넷 연결이 ㅜ ㅜ";
    public static final String NO_RESULT_MSG = "검색 결과 없음";

    private final UseCase isOnline;
    private final UseCase getRecentKeywords;
    private final UseCase addRecentKeyword;
    private final AsyncUseCase searchBooks;
    private final AsyncUseCase searchTitles;

    private final BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView;
    private final BookListView bookListView;
    private final ProgressBarView progressBarView;

    public BookPresenter(UseCase isOnline, UseCase getRecentKeywords, UseCase addRecentKeyword, AsyncUseCase searchBooks, AsyncUseCase searchTitles,
                         BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView, BookListView bookListView, ProgressBarView progressBarView) {

        // use_case
        this.isOnline = isOnline;
        this.getRecentKeywords = getRecentKeywords;
        this.addRecentKeyword = addRecentKeyword;
        this.searchBooks = searchBooks;
        this.searchTitles = searchTitles;

        // view
        this.bookTitleAutoCompleteTextView = bookTitleAutoCompleteTextView;
        this.bookListView = bookListView;
        this.progressBarView = progressBarView;

        if(!isOnline())
            bookListView.showStateMsg(NOT_ONLINE_MSG);
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
                BookPresenter.this.bookTitleAutoCompleteTextView.setTitles(keywords);
            }
        }, 700);
    }

    public void searchTitles(String keyword){
        if(isOnline()) {
            searchTitles.execute(keyword, new AsyncUseCaseListener<Void, List<String>>() {
                @Override
                public void onBefore(Void beforeArgs) {
                }

                @Override
                public void onAfter(List<String> afterArg) {
                    Log.i("UPDATE", "3");
                    bookTitleAutoCompleteTextView.setTitles(afterArg);
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }else{
            bookListView.showStateMsg(NOT_ONLINE_MSG);
        }
    }

    public void searchBooks(String keyword) {
        if(isOnline()) {

            Log.i(TAG, "entered keyword = " + keyword + "\n search start");
            addRecentKeyword.execute(keyword);
            Log.i(TAG, "addRecentKeyword = " + keyword);

            searchBooks.execute(keyword, new AsyncUseCaseListener<Void, List<Book>>() {
                @Override
                public void onBefore(Void Void) {
                    bookTitleAutoCompleteTextView.dismissDropDown();
                    bookTitleAutoCompleteTextView.clearFocus();
                    bookListView.hideKeyboard();

                    progressBarView.visible();
                    bookListView.clear();
                }

                @Override
                public void onAfter(List<Book> books) {
                    progressBarView.gone();
                    bookListView.addAll(books);
                    if(books.isEmpty()) {
                        bookListView.showStateMsg(NO_RESULT_MSG);
                    }else{
                        bookListView.showList();
                    }
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }else{
            bookListView.showStateMsg(NOT_ONLINE_MSG);
        }
    }

    private boolean isOnline(){
        return (boolean) isOnline.execute(null);
    }
}
