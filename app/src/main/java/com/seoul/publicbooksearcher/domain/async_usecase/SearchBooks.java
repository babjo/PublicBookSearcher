package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;
import android.os.Build;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.BookSearchException;
import com.seoul.publicbooksearcher.domain.SearchResult;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchBooks implements AsyncUseCase<String> {

    private static final String TAG = SearchBooks.class.getName();
    private AsyncUseCaseListener asyncUseCaseListener;

    private Map<String, BookRepository> bookRepositoryMap;
    private List<LibraryAsyncTask> libraryAsyncTaskList = new ArrayList();


    public SearchBooks(Map<String, BookRepository> bookRepositoryMap){
        this.bookRepositoryMap = bookRepositoryMap;

        for(String library : bookRepositoryMap.keySet())
            libraryAsyncTaskList.add(new LibraryAsyncTask(library, bookRepositoryMap.get(library)));

    }

    @Override
    public void execute(String keyword, final AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;

        for(LibraryAsyncTask libraryAsyncTask : libraryAsyncTaskList) {
            if (Build.VERSION.SDK_INT >= 11)
                libraryAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keyword);
            else
                libraryAsyncTask.execute(keyword);
        }

    }

    private class LibraryAsyncTask extends AsyncTask<String, Void, SearchResult> {

        private BookRepository bookRepository;
        private String library;

        public LibraryAsyncTask(String library, BookRepository bookRepository){
            this.bookRepository = bookRepository;
            this.library = library;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncUseCaseListener.onBefore(library);
        }

        @Override
        protected SearchResult doInBackground(String... params) {
            try {
                return new SearchResult(library, bookRepository.selectByKeyword(params[0]));
            }catch (Exception e){
                asyncUseCaseListener.onError(new BookSearchException(e.getMessage(), library));
                return null;
            }
        }
        @Override
        protected void onPostExecute(SearchResult searchResult) {
            super.onPostExecute(searchResult);
            if(searchResult != null)
                asyncUseCaseListener.onAfter(searchResult);
        }
    }
}

