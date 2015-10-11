package com.seoul.publicbooksearcher.domain;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.data.GdLibrary;
import com.seoul.publicbooksearcher.data.SeoulLibrary;
import com.seoul.publicbooksearcher.presentation.listener.SearchBooksListener;
import com.seoul.publicbooksearcher.presentation.listener.SearchTitlesListener;

import java.util.List;

public class SearchBooks implements UseCase <Void, String> {

    private static final String TAG = SearchBooks.class.getName();
    private final BookRepository gdLibrary;
    private final BookRepository seoulLibrary;

    private final SearchBooksListener searchBooksListener;
    private final SearchTitlesListener searchTitlesListener;

    public SearchBooks(SearchBooksListener searchBooksListener, SearchTitlesListener searchTitlesListener){
        this.searchBooksListener = searchBooksListener;
        this.searchTitlesListener = searchTitlesListener;

        this.gdLibrary = new GdLibrary();
        this.seoulLibrary = new SeoulLibrary();
    }

    @Override
    public Void execute(String keyword) {
        onceSearchBefore = false;
        new SeoulLibraryAsyncTask().execute(keyword);
        new GdLibraryAsyncTask().execute(keyword);
        return null;
    }

    private boolean onceSearchBefore = false;
    private void searchBefore(){
        if(!onceSearchBefore) {
            searchBooksListener.searchBefore();
            searchTitlesListener.searchBefore();
            onceSearchBefore = true;
        }
    }
    private class GdLibraryAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchBefore();
        }

        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks.this.gdLibrary.selectByKeyword(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            searchBooksListener.searchCompleted(books);
        }
    }

    private class SeoulLibraryAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchBefore();
        }

        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks.this.seoulLibrary.selectByKeyword(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            searchBooksListener.searchCompleted(books);
        }
    }
}

