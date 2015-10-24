package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;
import android.os.Build;

import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.BookSearchException;
import com.seoul.publicbooksearcher.domain.Library;
import com.seoul.publicbooksearcher.domain.SearchResult;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import java.util.Map;

public class SearchBooks implements AsyncUseCase<String> {

    private static final String TAG = SearchBooks.class.getName();
    private AsyncUseCaseListener asyncUseCaseListener;

    private Map<Library, BookRepository> bookRepositoryMap;

    public SearchBooks(Map<Library, BookRepository> bookRepositoryMap){
        this.bookRepositoryMap = bookRepositoryMap;
    }

    @Override
    public void execute(String keyword, final AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;

        for(Library library : bookRepositoryMap.keySet()){
            if (Build.VERSION.SDK_INT >= 11)
                new LibraryAsyncTask(library.getName(), bookRepositoryMap.get(library)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keyword);
            else
                new LibraryAsyncTask(library.getName(), bookRepositoryMap.get(library)).execute(keyword);
        }

    }

    private class LibraryAsyncTask extends AsyncTask<String, Void, SearchResult> {

        private BookRepository bookRepository;
        private String libraryName;

        public LibraryAsyncTask(String libraryName, BookRepository bookRepository){
            this.bookRepository = bookRepository;
            this.libraryName = libraryName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncUseCaseListener.onBefore(libraryName);
        }

        @Override
        protected SearchResult doInBackground(String... params) {
            try {
                return new SearchResult(new Library(libraryName, bookRepository.selectByKeyword(params[0])));
            }catch (Exception e){
                asyncUseCaseListener.onError(new BookSearchException(e.getMessage(), libraryName));
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

