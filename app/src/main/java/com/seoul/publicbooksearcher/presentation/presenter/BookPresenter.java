package com.seoul.publicbooksearcher.presentation.presenter;

import android.os.Handler;
import android.text.Html;
import android.util.Log;

import com.seoul.publicbooksearcher.domain.BookSearchException;
import com.seoul.publicbooksearcher.domain.Location;
import com.seoul.publicbooksearcher.domain.SearchResult;
import com.seoul.publicbooksearcher.domain.async_usecase.AsyncUseCase;
import com.seoul.publicbooksearcher.domain.usecase.UseCase;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;
import com.seoul.publicbooksearcher.presentation.view.component.BookListView;
import com.seoul.publicbooksearcher.presentation.view.component.BookTitleAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class BookPresenter {

    private final static String TAG = BookPresenter.class.getName();

    private final UseCase isOnline;
    private final UseCase getRecentKeywords;
    private final UseCase addRecentKeyword;
    private final AsyncUseCase searchBooks;
    private final AsyncUseCase searchTitles;
    private final AsyncUseCase sortLibraries;

    private final BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView;
    private final BookListView bookListView;

    public BookPresenter(UseCase isOnline, UseCase getRecentKeywords, UseCase addRecentKeyword, AsyncUseCase searchBooks, AsyncUseCase searchTitles, AsyncUseCase sortLibraries,
                         BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView, BookListView bookListView) {

        // use_case
        this.isOnline = isOnline;
        this.getRecentKeywords = getRecentKeywords;
        this.addRecentKeyword = addRecentKeyword;
        this.searchBooks = searchBooks;
        this.searchTitles = searchTitles;
        this.sortLibraries = sortLibraries;

        // view
        this.bookTitleAutoCompleteTextView = bookTitleAutoCompleteTextView;
        this.bookListView = bookListView;
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

    public void searchTitles(final String keyword){
            searchTitles.execute(keyword, new AsyncUseCaseListener<Void, List<String>, Exception>() {
                @Override
                public void onBefore(Void beforeArgs) {
                }

                @Override
                public void onAfter(List<String> afterArg) {
                    Log.i("UPDATE", "3");

                    List highlighted = new ArrayList();
                    for(String title : afterArg)
                        highlighted.add(highlight(title));

                    bookTitleAutoCompleteTextView.setTitles(highlighted);
                }

                private Object highlight(String title) {
                    return Html.fromHtml(title.replace(keyword, "<font color=\"red\">"+keyword+"</font>"));
                }

                @Override
                public void onError(Exception e) {
                }
            });

    }


    public void searchBooks(String keyword) {

            Log.i(TAG, "entered keyword = " + keyword + "search start");
            addRecentKeyword.execute(keyword);
            Log.i(TAG, "addRecentKeyword = " + keyword);

            searchBooks.execute(keyword, new AsyncUseCaseListener<String, SearchResult, BookSearchException>() {
                @Override
                public void onBefore(final String library) {
                    bookTitleAutoCompleteTextView.dismissDropDown();
                    bookTitleAutoCompleteTextView.clearFocus();

                    new Handler().post(new Runnable() { // new Handler and Runnable
                        @Override
                        public void run() {
                            bookListView.collapseAllParents();
                            bookListView.clearLibrary(library);
                        }
                    });

                    bookListView.progressVisible(library);
                    bookListView.hideKeyboard();
                }

                @Override
                public void onAfter(SearchResult searchResult) {
                    bookListView.updateLibrary(searchResult.getLibraryName(), searchResult.getBooks());
                    bookListView.progressGone(searchResult.getLibraryName());
                }

                @Override
                public void onError(BookSearchException e) {
                    bookListView.showError(e.getLibrary(), e.getMessage());
                    //bookListView.progressGone(e.getLibrary());
                }
            });

    }

    public void sortLibraries(){
        sortLibraries.execute(null, new AsyncUseCaseListener<Void, Location, RuntimeException>() {
            @Override
            public void onBefore(Void beforeArgs) {}

            @Override
            public void onAfter(Location location) {
                Log.i(TAG, "=========== current location : "+location.latitude + ", " + location.longitude);
                bookListView.sort(location);
            }

            @Override
            public void onError(RuntimeException e) {}
        });
    }

    private boolean isOnline(){
        return (boolean) isOnline.execute(null);
    }
}
