package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;
import android.os.Handler;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.Library;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.Map;

public class SearchBooks implements AsyncUseCase<String> {

    private static final String TAG = SearchBooks.class.getName();
    private AsyncUseCaseListener asyncUseCaseListener;

    private Map<String, BookRepository> bookRepositoryMap;


    public SearchBooks(Map<String, BookRepository> bookRepositoryMap){
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

