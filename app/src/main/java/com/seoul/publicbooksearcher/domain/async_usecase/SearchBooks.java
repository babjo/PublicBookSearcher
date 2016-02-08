package com.seoul.publicbooksearcher.domain.async_usecase;

import android.os.AsyncTask;
import android.os.Build;

import com.seoul.publicbooksearcher.domain.Library;
import com.seoul.publicbooksearcher.domain.SearchResult;
import com.seoul.publicbooksearcher.domain.exception.BookSearchException;
import com.seoul.publicbooksearcher.infrastructure.crawler.book.BookCrawler;
import com.seoul.publicbooksearcher.infrastructure.crawler.book.BookCrawlerCollection;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class SearchBooks implements AsyncUseCase<String> {

    @Bean(BookCrawlerCollection.class)
    BookCrawlerCollection bookCrawlerCollection;

    private static final String TAG = SearchBooks.class.getName();
    private AsyncUseCaseListener asyncUseCaseListener;

    private List<LibraryAsyncTask> libraryAsyncTaskList = new ArrayList();

    @Override
    public void execute(String keyword, final AsyncUseCaseListener asyncUseCaseListener) {
        this.asyncUseCaseListener = asyncUseCaseListener;

        for(LibraryAsyncTask libraryAsyncTask : libraryAsyncTaskList)
            libraryAsyncTask.cancel(false);
        libraryAsyncTaskList.clear();


        for(BookCrawler bookCrawler : bookCrawlerCollection.get()){
            LibraryAsyncTask libraryAsyncTask = new LibraryAsyncTask(bookCrawler);
            libraryAsyncTaskList.add(libraryAsyncTask);
            if (Build.VERSION.SDK_INT >= 11)
                libraryAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keyword);
            else
                libraryAsyncTask.execute(keyword);
        }

    }

    private class LibraryAsyncTask extends AsyncTask<String, Void, SearchResult> {

        private BookCrawler bookCrawler;

        public LibraryAsyncTask(BookCrawler bookCrawler){
            this.bookCrawler = bookCrawler;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncUseCaseListener.onBefore(bookCrawler.getLibraryId());
        }

        @Override
        protected SearchResult doInBackground(String... params) {
            int count = 0;
            int maxTries = 2;
            while(true) {
                try {
                    return new SearchResult(new Library(bookCrawler.getLibraryId(), bookCrawler.crawling(params[0])));
                } catch (Exception e) {
                    if (++count == maxTries){
                        asyncUseCaseListener.onError(new BookSearchException(e.getMessage(), bookCrawler.getLibraryId()));
                        return null;
                    }
                }
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

