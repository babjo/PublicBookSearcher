package com.seoul.publicbooksearcher.domain;

import android.content.Context;
import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.data.cache.GdBookCache;
import com.seoul.publicbooksearcher.data.cache.SeoulBookCache;
import com.seoul.publicbooksearcher.data.crawler.GdLibrary;
import com.seoul.publicbooksearcher.data.crawler.SeoulLibrary;
import com.seoul.publicbooksearcher.presentation.listener.SearchBooksListener;
import com.seoul.publicbooksearcher.presentation.listener.SearchTitlesListener;

import java.util.List;

public class SearchBooks implements UseCase <Void, String> {

    private static final String TAG = SearchBooks.class.getName();
    private final BookRepository gdLibrary;
    private final BookRepository seoulLibrary;

    private final SearchBooksListener searchBooksListener;
    private final SearchTitlesListener searchTitlesListener;

    public SearchBooks(Context context, SearchBooksListener searchBooksListener, SearchTitlesListener searchTitlesListener){
        this.searchBooksListener = searchBooksListener;
        this.searchTitlesListener = searchTitlesListener;

        this.gdLibrary = new GdLibrary(new GdBookCache(context));
        this.seoulLibrary = new SeoulLibrary(new SeoulBookCache(context));
    }

    @Override
    public Void execute(String keyword) {
        onceSearchBefore = false;
        new GdLibraryAsyncTask().execute(keyword);
        new SeoulLibraryAsyncTask().execute(keyword);
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

    private abstract class LibraryAsyncTas extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchBefore();
        }

        @Override
        protected  abstract List<Book> doInBackground(String... params);

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            searchBooksListener.searchCompleted(books);
        }
    }
    private class GdLibraryAsyncTask extends LibraryAsyncTas {
        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks.this.gdLibrary.selectByKeyword(params[0]);
        }
    }

    private class SeoulLibraryAsyncTask extends LibraryAsyncTas{
        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks.this.seoulLibrary.selectByKeyword(params[0]);
        }
    }
}

