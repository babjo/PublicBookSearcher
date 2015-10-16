package com.seoul.publicbooksearcher.domain;

import android.content.Context;
import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.data.cache.book.GdBookCache;
import com.seoul.publicbooksearcher.data.cache.book.SeoulBookCache;
import com.seoul.publicbooksearcher.data.crawler.GdLibrary;
import com.seoul.publicbooksearcher.data.crawler.SeoulLibrary;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.List;

public class SearchBooks2 implements AsyncUseCase<String> {

    private static final String TAG = SearchBooks2.class.getName();
    private final BookRepository gdLibrary;
    private final BookRepository seoulLibrary;
    private AsyncUseCaseListener asyncUseCaseListener;

    public SearchBooks2(BookRepository gdLibrary, BookRepository seoulLibrary){
        this.gdLibrary = gdLibrary;
        this.seoulLibrary = seoulLibrary;
    }

    /*
    private boolean onceSearchBefore = false;

    private void searchBefore(){
        if(!onceSearchBefore) {
            searchBooksListener.onBefore(null);
            searchTitlesListener.onBefore(null);
            onceSearchBefore = true;
        }
    }*/


    @Override
    public void execute(String keyword, AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;
        new GdLibraryAsyncTask().execute(keyword);
        new SeoulLibraryAsyncTask().execute(keyword);
    }

    private abstract class LibraryAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncUseCaseListener.onBefore(null);
        }

        @Override
        protected  abstract List<Book> doInBackground(String... params);

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            asyncUseCaseListener.onAfter(books);
        }
    }
    private class GdLibraryAsyncTask extends LibraryAsyncTask {
        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks2.this.gdLibrary.selectByKeyword(params[0]);
        }
    }

    private class SeoulLibraryAsyncTask extends LibraryAsyncTask {
        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks2.this.seoulLibrary.selectByKeyword(params[0]);
        }
    }
}

