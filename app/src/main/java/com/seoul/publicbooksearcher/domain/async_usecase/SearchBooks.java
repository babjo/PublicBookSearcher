package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

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
            return SearchBooks.this.gdLibrary.selectByKeyword(params[0]);
        }
    }

    private class SeoulLibraryAsyncTask extends LibraryAsyncTask {
        @Override
        protected List<Book> doInBackground(String... params) {
            return SearchBooks.this.seoulLibrary.selectByKeyword(params[0]);
        }
    }
}

