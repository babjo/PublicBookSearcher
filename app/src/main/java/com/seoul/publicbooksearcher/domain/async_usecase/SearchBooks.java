package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.Library;
import com.seoul.publicbooksearcher.domain.SearchResult;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchBooks implements AsyncUseCase<String> {

    private static final String TAG = SearchBooks.class.getName();
    private final BookRepository gdLibrary;
    private final BookRepository seoulLibrary;
    private AsyncUseCaseListener asyncUseCaseListener;

    public SearchBooks(BookRepository gdLibrary, BookRepository seoulLibrary){
        this.gdLibrary = gdLibrary;
        this.seoulLibrary = seoulLibrary;
    }

    @Override
    public void execute(String keyword, AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;
        try {
            new GdLibraryAsyncTask().execute(keyword);
            new SeoulLibraryAsyncTask().execute(keyword);
            //new LibraryAsyncTask().execute(keyword);
        }catch (Exception e){
            asyncUseCaseListener.onError(e);
        }
    }

    /*
    private class LibraryAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncUseCaseListener.onBefore(null);
        }

        @Override
        protected  List<Book> doInBackground(String... params){
            List<Book> books = new ArrayList();
            books.addAll(SearchBooks.this.seoulLibrary.selectByKeyword(params[0]));
            books.addAll(SearchBooks.this.gdLibrary.selectByKeyword(params[0]));
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            asyncUseCaseListener.onAfter(books);
        }
    }*/

    private abstract class LibraryAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncUseCaseListener.onBefore(null);
        }

        @Override
        protected abstract List<Book> doInBackground(String... params);

    }
    private class GdLibraryAsyncTask extends LibraryAsyncTask {
        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks.this.gdLibrary.selectByKeyword(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            asyncUseCaseListener.onAfter(books);
        }
    }

    private class SeoulLibraryAsyncTask extends LibraryAsyncTask {
        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks.this.seoulLibrary.selectByKeyword(params[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            SearchResult searchResult = new SearchResult();
            if(books.size() == 0)
                searchResult.addEmptyLibrary("서울도서관");
            else
                searchResult.addLibrary(new Library("서울도서관", books));
            asyncUseCaseListener.onAfter(searchResult);
        }
    }

}
