package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;
import android.os.Handler;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.Library;
import com.seoul.publicbooksearcher.domain.SearchResult;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchBooks2 implements AsyncUseCase<String> {

    private static final String TAG = SearchBooks2.class.getName();
    private AsyncUseCaseListener asyncUseCaseListener;

    private Map<String, BookRepository> bookRepositoryMap;


    public SearchBooks2(Map<String, BookRepository> bookRepositoryMap){
        this.bookRepositoryMap = bookRepositoryMap;
    }

    @Override
    public void execute(String keyword, final AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                asyncUseCaseListener.onBefore(null);
            }
        });

        try {
            for(String library : bookRepositoryMap.keySet()){
                new LibraryAsyncTask(library, bookRepositoryMap.get(library)).execute(keyword);
            }
        }catch (Exception e){
            asyncUseCaseListener.onError(e);
        }
    }

    private class LibraryAsyncTask extends AsyncTask<String, Void, Library> {

        private BookRepository bookRepository;
        private String library;

        public LibraryAsyncTask(String library, BookRepository bookRepository){
            this.bookRepository = bookRepository;
            this.library = library;
        }

        @Override
        protected Library doInBackground(String... params) {
            return new Library(library, bookRepository.selectByKeyword(params[0]));
        }
        @Override
        protected void onPostExecute(Library library) {
            super.onPostExecute(library);
            asyncUseCaseListener.onAfter(library);
        }
    }
}

